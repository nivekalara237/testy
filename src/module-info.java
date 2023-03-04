module com.nivekaa.testy {
  requires org.postgresql.jdbc;
  requires transitive java.sql;
  requires static java.compiler;
  // requires transitive com.nivekaa.client;
  requires transitive com.nivekaa.commons;

  uses java.nio.file.spi.FileSystemProvider;
  uses com.nivekaa.service.spi;
}
