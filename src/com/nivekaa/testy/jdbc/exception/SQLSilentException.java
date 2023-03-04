package com.nivekaa.testy.jdbc.exception;

public class SQLSilentException extends RuntimeException {
  public SQLSilentException(String message) {
    super(message);
  }

  public SQLSilentException(Throwable throwable) {
    super(throwable);
  }
}
