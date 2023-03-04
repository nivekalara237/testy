package com.nivekaa.testy.jdbc;

import com.nivekaa.testy.jdbc.exception.SQLSilentException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class ConnectionUtils {

  public static final ConnectionUtils INSTANCE = new ConnectionUtils();

  private ConnectionUtils() {}

  public static Connection getConnection(String url, String username, String password) {
    Map<String, ?> props =
        Map.ofEntries(Map.entry("user", username), Map.entry("password", password));
    return getConnection(url, props);
  }

  public static Connection getConnection(String url) {
      return getConnection(url, Map.of());
  }

  public static Connection getConnection(String url, Map<String, ?> props) {
    Connection conn;
    try {
      if (props.isEmpty()) {
        conn = DriverManager.getConnection(url);
      } else {
          if (props.size() < 2) {
              throw new SQLSilentException("The props needs at least two properties, which is equivalent of username and password first");
          }
        Properties properties = new Properties();
        properties.putAll(props);
        properties.putIfAbsent("ssl", "false");
        conn = DriverManager.getConnection(url, properties);
      }
    } catch (SQLException sqlException) {
        String message = """
                Error while trying the connection in the Database :
                Message Error: %s
                Error Code: %s
                SQl State: %s
                """.formatted(sqlException.getMessage(), sqlException.getErrorCode(), sqlException.getSQLState());
        throw new SQLSilentException(message);
    }
    return conn;
  }

  public Connection createConnection() {
      Map<String, String> props =
              Map.ofEntries(
                      Map.entry("user", "dbuser"),
                      Map.entry("password", "dbpass"),
                      Map.entry("currentSchema", "testydb,public"),
                      Map.entry("applicationName", "testy-app"));
      return ConnectionUtils.getConnection("jdbc:postgresql://localhost:5432/dbracing", props);
  }
}
