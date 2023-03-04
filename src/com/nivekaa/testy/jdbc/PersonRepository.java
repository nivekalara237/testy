package com.nivekaa.testy.jdbc;

import com.nivekaa.testy.annotation.QueryScript;
import com.nivekaa.testy.example.Person;

import java.util.Optional;

public interface PersonRepository extends Repository<Person, Integer> {
  @QueryScript(script = "Select * from Person where id = :id", model = Person.class)
  Optional<Person> getPersonById(Integer id);
}
