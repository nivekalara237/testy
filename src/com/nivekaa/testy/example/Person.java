package com.nivekaa.testy.example;

import com.nivekaa.testy.annotation.Table;
import com.nivekaa.testy.annotation.TableColumn;

import java.io.Serializable;

@Table(name = "person")
public class Person implements Serializable {
  @TableColumn(columnName = "id")
  private int id;

  @TableColumn(columnName = "name")
  private String fullname;

  @TableColumn private int age;

  @TableColumn private String address;

  public Person() {
    /* TODO document why this constructor is empty */
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Person{"
        + "id="
        + id
        + ", fullname='"
        + fullname
        + '\''
        + ", age="
        + age
        + ", address='"
        + address
        + '\''
        + '}';
  }
}
