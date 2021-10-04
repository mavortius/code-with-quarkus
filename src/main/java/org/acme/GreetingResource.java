package org.acme;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GreetingResource {
  private final EventBus eventBus;
  private final GreetingService service;
  private final MeterRegistry meterRegistry;

  public GreetingResource(EventBus eventBus, GreetingService service, MeterRegistry meterRegistry) {
    this.eventBus = eventBus;
    this.service = service;
    this.meterRegistry = meterRegistry;
  }

  @GET
  @Path("hello")
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return service.getGreeting();
  }

  @GET
  @Path("hello/{name}")
  @Counted
  @Produces(MediaType.TEXT_PLAIN)
  public String sayHello(@PathParam("name") String name) {
    meterRegistry.counter("greeting_counter", Tags.of("name", name)).increment();
    return "Hello " + name;
  }

  @GET
  @Path("async/{name}")
  @Produces(MediaType.TEXT_PLAIN)
  public Uni<String> greeting(@PathParam("name") String name) {
    return eventBus.<String>request("greeting", name).map(Message::body);
  }

  @GET
  @Path("async/block/{message}")
  @Produces(MediaType.TEXT_PLAIN)
  public Uni<String> blockingConsumer(@PathParam("message") String message) {
    return eventBus.<String>request("blocking-consumer", message).map(Message::body);
  }
}