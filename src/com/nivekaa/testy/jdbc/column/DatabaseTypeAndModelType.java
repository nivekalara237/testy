package com.nivekaa.testy.jdbc.column;

public record DatabaseTypeAndModelType(Class<?> modelType, Class<?> dbTypeEquivalent) {

  public Class<?> getDbTypeForModelPrimitiveType() {
    if (this.modelType.isPrimitive()) {
      return ColumnTableKeyValue.primitiveToObject(this.modelType);
    }
    return this.modelType;
  }

  public static DatabaseTypeAndModelType fromModelType(Class<?> modelType) {
    var dbtype = modelType;
    if (modelType.isPrimitive()) {
      dbtype = ColumnTableKeyValue.primitiveToObject(modelType);
    }
    return new DatabaseTypeAndModelType(modelType, dbtype);
  }
}
