package com.nivekaa.testy.jdbc.column;

import com.nivekaa.testy.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ValorizeTableColumnValueToModel<T> {

  public static <T> ValorizeTableColumnValueToModel getInstance() {
    return new ValorizeTableColumnValueToModel<T>();
  }

  private ValorizeTableColumnValueToModel() {}

  public void valorizedModel(T modelCaller, Object dbValue, String modelAttr, Class<?> attrType) {
    Class<T> tClass = (Class<T>) modelCaller.getClass();
    try {
      Method method =
          tClass.getDeclaredMethod(StringUtils.getSetterMethodByFieldName(modelAttr), attrType);
      method.invoke(modelCaller, dbValue);
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }
}
