package org.acme.fruits;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class Fruit {
  @NotBlank
  private String name;
  private String description;

  public Fruit() {
  }

  public Fruit(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
