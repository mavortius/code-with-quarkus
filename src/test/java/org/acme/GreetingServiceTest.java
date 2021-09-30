package org.acme;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GreetingServiceTest {

  @Test
  void getGreetingOk() {
    assertEquals("Hello Quarkus", new GreetingService("Quarkus").getGreeting());
  }
}