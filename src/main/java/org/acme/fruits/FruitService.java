package org.acme.fruits;

import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class FruitService {
  private final ConcurrentMap<String, Fruit> fruits = new ConcurrentHashMap<>();

  public FruitService() {
    this.fruits.put("apple", new Fruit("Apple", "Winter fruit"));
    this.fruits.put("pineapple", new Fruit("Pineapple", "Tropical fruit"));
  }

  public Collection<Fruit> getFruits() {
    return fruits.values();
  }

  public Uni<Fruit> getFruit(String name) {
    return Uni.createFrom().item(fruits.get(name.toLowerCase()));
  }

  public void addFruit(Fruit fruit) {
    fruits.put(fruit.getName(), fruit);
  }

  public void deleteFruit(String name) {
    Fruit fruit = fruits.get(name);

    if (fruit == null) {
      throw new NotFoundException("Fruit not found");
    }

    fruits.remove(name);
  }
}
