package com.nivekaa.testy.jdbc.column;

import java.util.Map;
import java.util.stream.Collectors;

public record ColumnTableKeyValue(String databaseColumnName, String modelColumnName, DatabaseTypeAndModelType type) {
  private static final Map<Class<?>, Class<?>> mapPrimitiveWrapper = Map.ofEntries(
      Map.entry(boolean.class, Boolean.class),
      Map.entry(int.class, Integer.class),
      Map.entry(byte.class, Byte.class),
      Map.entry(char.class, Character.class),
      Map.entry(long.class, Long.class),
      Map.entry(float.class, Float.class),
      Map.entry(short.class, Short.class),
      Map.entry(double.class, Double.class),
      Map.entry(void.class, Void.class)
  );

  public static Class<?> primitiveToObject(Class<?> primitiveClass) {
    return mapPrimitiveWrapper.get(primitiveClass);
  }

  public static Class<?> objectToPrimitive(Class<?> objectClass) {
    return mapPrimitiveWrapper.entrySet()
        .stream()
        .map(classClassEntry -> Map.entry(classClassEntry.getValue(), classClassEntry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        .get(objectClass);
  }
}
