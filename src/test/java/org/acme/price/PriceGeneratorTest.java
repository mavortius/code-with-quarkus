package org.acme.price;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PriceGeneratorTest {
  PriceGenerator priceGenerator = new PriceGenerator();

  @Test
  void generatesProperly() {
    List<Integer> prices = priceGenerator.generate()
            .select().first(2)
            .subscribe().withSubscriber(AssertSubscriber.create(2))
            .assertSubscribed()
            .awaitNextItem(Duration.ofSeconds(10))
            .awaitNextItem(Duration.ofSeconds(10))
            .awaitCompletion(Duration.ofSeconds(15))
            .assertCompleted()
            .getItems();

    assertThat(prices)
            .hasSize(2)
            .allMatch(value -> (value >= 0) && (value < 100));
  }
}