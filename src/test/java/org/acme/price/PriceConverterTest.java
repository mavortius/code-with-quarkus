package org.acme.price;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PriceConverterTest {
  PriceConverter priceConverter = new PriceConverter();

  @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER + "[" + ParameterizedTest.INDEX_PLACEHOLDER + "] (" +
          ParameterizedTest.ARGUMENTS_WITH_NAMES_PLACEHOLDER + ")")
  @ValueSource(ints = {1, 2})
  @DisplayName("process")
  void process(int price) {
    assertThat(priceConverter.process(price))
            .isEqualTo(price * PriceConverter.CONVERSION_RATE);
  }
}