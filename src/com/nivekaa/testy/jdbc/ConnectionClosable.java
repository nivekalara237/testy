package com.nivekaa.testy.jdbc;

import java.sql.Connection;

public interface ConnectionClosable {
  void closeConnection(Connection connection);
}
