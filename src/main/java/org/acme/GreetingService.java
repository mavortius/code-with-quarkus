package org.acme;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {
  private final String greeting;

  public GreetingService(@ConfigProperty(name = "greeting.name", defaultValue = "Quarkus") String greeting) {
    this.greeting = greeting;
  }

  public String getGreeting() {
    return "Hello " + greeting;
  }

  @ConsumeEvent("greeting")
  public String consume(String name) {
    return name.toUpperCase();
  }

  @Blocking
  @ConsumeEvent("blocking-consumer")
  public String consumeBlocking(String message) {
    return "Processing Blocking I/O: " + message;
  }
}
