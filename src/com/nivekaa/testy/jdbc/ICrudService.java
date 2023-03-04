package com.nivekaa.testy.jdbc;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public sealed interface ICrudService<T extends Serializable> permits CrudOperationService {
  Optional<T> getSingleRecord(String sqlString);
  List<T> getRecords(String sqlString);
  int insert(String sqlString);
  int update(String sqlString);
  int delete(String sqlString);
}
