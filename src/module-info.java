module com.nivekaa.testy {
  requires org.postgresql.jdbc;
  requires transitive java.sql;
  requires static java.compiler;
  // requires transitive com.nivekaa.client;
  requires transitive com.nivekaa.commons;
}
