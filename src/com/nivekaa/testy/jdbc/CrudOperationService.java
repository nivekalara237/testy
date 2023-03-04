package com.nivekaa.testy.jdbc;

import com.nivekaa.testy.annotation.DatabaseTableMatadataChecker;
import com.nivekaa.testy.annotation.TableColumn;
import com.nivekaa.testy.jdbc.column.ColumnTableKeyValue;
import com.nivekaa.testy.jdbc.column.DatabaseTypeAndModelType;
import com.nivekaa.testy.jdbc.column.ValorizeTableColumnValueToModel;
import com.nivekaa.testy.jdbc.exception.SQLSilentException;
import com.nivekaa.testy.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class CrudOperationService<T extends Serializable>
    implements ICrudService<T>, ConnectionClosable {
  private final Connection connection;
  private final Class<T> typeObject;

  private void validateEntityClass() {
    DatabaseTableMatadataChecker checker = new DatabaseTableMatadataChecker(typeObject);
    if (!checker.classIsTableAnnotated()) {
      throw new SQLSilentException(
          "The entity class %s must be annotated by @Table(name='table_name')"
              .formatted(typeObject.getSimpleName()));
    }

    if (!checker.annotationTableHasNameDefined()) {
      throw new SQLSilentException(
          "The entity class %s is annotated by @Table but with attribute name not defined"
              .formatted(typeObject.getSimpleName()));
    }
  }

  public CrudOperationService(Connection conn, Class<T> tClass) {
    this.connection = conn;
    this.typeObject = tClass;
    this.validateEntityClass();
  }

  @Override
  public void closeConnection(Connection connection) {
    try {
      if (connection != null && connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException sqlException) {
      throw new SQLSilentException(
          "Unable to close connection. [%s]".formatted(sqlException.getMessage()));
    }
  }

  private T createInstanceOfModel() {
    try {
      return typeObject.getDeclaredConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public Optional<T> getSingleRecord(String sqlString) {
    T result = createInstanceOfModel();
    try (PreparedStatement statement = this.connection.prepareStatement(sqlString);
        ResultSet resultSet = statement.executeQuery()) {
      ResultSetMetaData metaData = statement.getMetaData();
      List<ColumnTableKeyValue> columnTableKeyValues = getColumnTableKeyValues();

      int columnCount = metaData.getColumnCount();

      if (resultSet.next()) {
        for (int i = 1; i <= columnCount; i++) {
          getDataLine(result, metaData.getColumnName(i), resultSet, columnTableKeyValues);
        }
      } else {
        return Optional.empty();
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new SQLSilentException(
          "Unable to execute query provided. [%s]".formatted(sqlException.getMessage()));
    } finally {
      closeConnection(connection);
    }
    return Optional.of(result);
  }

  private void getDataLine(
      T result,
      String columnName,
      ResultSet resultSet,
      List<ColumnTableKeyValue> columnTableKeyValues)
      throws SQLException {
    ColumnTableKeyValue columnTableKeyValue =
        columnTableKeyValues.stream()
            .filter(
                columnTableKeyValue1 ->
                    Objects.equals(columnTableKeyValue1.databaseColumnName(), columnName))
            .findFirst()
            .orElseThrow(() -> new SQLSilentException("LKMLKMK"));

    ValorizeTableColumnValueToModel<T> valorizer = ValorizeTableColumnValueToModel.<T>getInstance();
    valorizer.valorizedModel(
        result,
        resultSet.getObject(columnName, columnTableKeyValue.type().dbTypeEquivalent()),
        columnTableKeyValue.modelColumnName(),
        columnTableKeyValue.type().modelType());
  }

  private List<ColumnTableKeyValue> getColumnTableKeyValues() {
    return Arrays.stream(typeObject.getDeclaredFields())
        .map(
            field -> {
              String modelColumnName = field.getName();
              String dbColumnName = modelColumnName;
              DatabaseTypeAndModelType type =
                  DatabaseTypeAndModelType.fromModelType(field.getType());
              if (field.isAnnotationPresent(TableColumn.class)) {
                TableColumn tableColumn = field.getAnnotation(TableColumn.class);
                dbColumnName =
                    (StringUtils.isBlank(tableColumn.columnName())
                            || tableColumn.columnName().equals("#"))
                        ? modelColumnName
                        : tableColumn.columnName();
              }
              return new ColumnTableKeyValue(dbColumnName, modelColumnName, type);
            })
        .toList();
  }

  @Override
  public List<T> getRecords(String sqlString) {
    List<T> results = new ArrayList<>();

    try (PreparedStatement statement = this.connection.prepareStatement(sqlString);
        ResultSet resultSet = statement.executeQuery()) {
      ResultSetMetaData metaData = statement.getMetaData();
      List<ColumnTableKeyValue> columnTableKeyValues = getColumnTableKeyValues();

      int columnCount = metaData.getColumnCount();

      while (resultSet.next()) {
        T item = createInstanceOfModel();
        for (int i = 1; i <= columnCount; i++) {
          getDataLine(item, metaData.getColumnName(i), resultSet, columnTableKeyValues);
        }
        results.add(item);
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new SQLSilentException(
          "Unable to execute query provided. [%s]".formatted(sqlException.getMessage()));
    } finally {
      closeConnection(connection);
    }

    return results;
  }

  @Override
  public int insert(String sqlString) {
    int numberRowAffected = 0;
    try (Statement statement = connection.createStatement()) {
      boolean successExecution = statement.execute(sqlString);
      if (!successExecution) {
        return 0;
      }
      // ResultSet resultSet = statement.getResultSet();
      numberRowAffected = statement.getUpdateCount();
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new SQLSilentException(
          "Unable to execute query provided. [%s]".formatted(sqlException.getMessage()));
    } finally {
      closeConnection(connection);
    }
    return numberRowAffected;
  }

  @Override
  public int update(String sqlString) {
    int rowsAffected;
    try (Statement statement = connection.createStatement()) {
      rowsAffected = statement.executeUpdate(sqlString);
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new SQLSilentException(
          "Unable to execute query provided. [%s]".formatted(sqlException.getMessage()));
    } finally {
      closeConnection(connection);
    }
    return rowsAffected;
  }

  @Override
  public int delete(String sqlString) {
    int rowsAffected;
    try (Statement statement = connection.createStatement()) {
      rowsAffected = statement.executeUpdate(sqlString);
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new SQLSilentException(
          "Unable to execute query provided. [%s]".formatted(sqlException.getMessage()));
    } finally {
      closeConnection(connection);
    }
    return rowsAffected;
  }
}
