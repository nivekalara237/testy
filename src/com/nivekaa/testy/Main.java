package com.nivekaa.testy;

import com.nivekaa.testy.example.Person;
import com.nivekaa.testy.jdbc.ConnectionUtils;
import com.nivekaa.testy.jdbc.CrudOperationService;

import java.nio.file.spi.FileSystemProvider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ServiceLoader;

public class Main {

  public static void main(String[] args) throws SQLException {
    Connection connection = ConnectionUtils.INSTANCE.createConnection();

    CrudOperationService<Person> crudOperationService =
        new CrudOperationService<>(connection, Person.class);

    /*System.err.println(
        new com.nivekaa.client.Person(
            Utils.capitalizer("kevin"), 90, "04 Ave Surath, Quatre Bornes, Mauritius"));
    */

    ServiceLoader<FileSystemProvider> loader = ServiceLoader.load(FileSystemProvider.class);

    Optional<FileSystemProvider> providerOptional = loader.findFirst();
    System.err.println(providerOptional.isPresent());

    /*System.err.println(
        crudOperationService.getSingleRecord("select * from person where id = 345;").isPresent());
    System.out.println("***************************************************");
    System.err.println(crudOperationService.getRecords("select * from person;"));*/
  }
}
