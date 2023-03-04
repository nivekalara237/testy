package com.nivekaa.testy.annotation;

import com.nivekaa.testy.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

public record DatabaseTableMatadataChecker(Class<?> clazz) {

  public boolean classIsTableAnnotated() {
    return isAnnotatedBy(this.clazz.getAnnotations(), Table.class);
  }

  private boolean isAnnotatedBy(Annotation[] annotations, Class<?> classe) {
    return Arrays.stream(annotations)
        .anyMatch(
            annotation ->
                Objects.equals(
                    annotation.annotationType().getSimpleName(), classe.getSimpleName()));
  }

  public boolean classAsFieldAnnotatedByTableColumn() {
    return Arrays.stream(this.clazz.getDeclaredFields())
        .anyMatch(field -> isAnnotatedBy(field.getAnnotations(), TableColumn.class));
  }

  public boolean annotationTableHasNameDefined() {
    return classIsTableAnnotated() && Arrays.stream(this.clazz.getAnnotations())
        .filter(annotation -> Objects.equals(
            annotation.annotationType().getSimpleName(), Table.class.getSimpleName()))
        .findFirst()
        .map(annotation -> StringUtils.isNotBlank(((Table)annotation).name()))
        .orElse(Boolean.FALSE);
  }
}
