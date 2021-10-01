package org.acme;

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

  public GreetingResource(EventBus eventBus, GreetingService service) {
    this.eventBus = eventBus;
    this.service = service;
  }

  @GET
  @Path("hello")
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return service.getGreeting();
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