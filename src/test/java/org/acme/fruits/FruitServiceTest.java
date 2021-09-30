package org.acme.fruits;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FruitServiceTest {
  private final FruitService fruitService = new FruitService();

  @Test
  void getFruits() {
    assertThat(fruitService.getFruits()).hasSize(2);
  }

  @Test
  void getFruitFound() {
    var fruit = fruitService.getFruit("apple")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .assertCompleted()
            .getItem();

    assertThat(fruit)
            .isNotNull()
            .extracting(Fruit::getName, Fruit::getDescription)
            .containsExactly("Apple", "Winter fruit");
  }

  @Test
  void getFruitNotFound() {
    fruitService.getFruit("pear")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .assertCompleted()
            .assertItem(null);
  }

  @Test
  void addFruit() {
    fruitService.addFruit(new Fruit("Pear", "Delicious fruit"));

    assertThat(fruitService.getFruits()).hasSize(3);
  }

  @Test
  void deleteFruit() {
    fruitService.deleteFruit("apple");

    assertThat(fruitService.getFruits()).hasSize(1);
  }

  @Test
  void deleteFruitNotFound() {
    NotFoundException foundException = assertThrows(NotFoundException.class, () -> fruitService.deleteFruit("pear"));
    assertThat(foundException.getMessage()).isEqualTo("Fruit not found");
  }
}