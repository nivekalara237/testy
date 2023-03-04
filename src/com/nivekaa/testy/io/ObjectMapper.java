package com.nivekaa.testy.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.nio.charset.StandardCharsets;

public class ObjectMapper {

  public <T> T readAsObjectFromString(String objetString, Class<T> tClass) {
    try {
      ObjectInputStream objectInputStream =
          new ObjectInputStream(
              new ByteArrayInputStream(objetString.getBytes(StandardCharsets.UTF_8)));
      for (ObjectStreamField field :
          objectInputStream.readFields().getObjectStreamClass().getFields()) {
        // field.

      }
      return (T) objectInputStream.readObject();
    } catch (Exception exception) {
      throw new IllegalArgumentException(
          "Unable to convert string given to Objet of type" + tClass.getSimpleName());
    }
  }

  public byte[] writeToByteArray(byte[] bytes) {
    try {
      ObjectOutputStream objectOutputStream =
          new ObjectOutputStream(new ByteArrayOutputStream(bytes.length));
      objectOutputStream.writeObject(bytes);
      return new byte[0];
    } catch (IOException exception) {
      throw new IllegalArgumentException("Unable to convert string given to Objet of type");
    }
  }
}
