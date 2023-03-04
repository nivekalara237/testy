package com.nivekaa.commons;

public class Utils {
  private Utils() {
    throw new IllegalCallerException("Unable the instance this helper class");
  }

  public static String capitalizer(String str) {
    if (str == null) {
      throw new NullPointerException("The string must not be null");
    } else if (str.isBlank()) {
      return str;
    } else {
      return Character.toString(str.charAt(0)).toUpperCase() + str.substring(1);
    }
  }
}
