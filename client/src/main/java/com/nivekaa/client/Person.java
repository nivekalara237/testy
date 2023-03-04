package com.nivekaa.client;

import java.util.Objects;

public record Person(String fullname, int age, String address) {

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return age == person.age && fullname.equals(person.fullname) && address.equals(person.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullname, age, address);
  }

  @Override
  public String toString() {
    return "Person{" +
        "fullname='" + fullname + '\'' +
        ", age=" + age +
        ", address='" + address + '\'' +
        '}';
  }
}
