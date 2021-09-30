package org.acme.fruits;

import io.smallrye.mutiny.Uni;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collection;

@Path("/fruits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FruitResource {
  private final FruitService service;

  public FruitResource(FruitService service) {
    this.service = service;
  }

  @GET
  public Collection<Fruit> list() {
    return service.getFruits();
  }

  @GET
  @Path("/{name}")
  public Uni<Response> getFruit(@PathParam("name") String name) {
    return service.getFruit(name)
            .onItem().ifNotNull().transform(fruit -> Response.ok(fruit).build())
            .onItem().ifNull().continueWith(Response.status(Status.NOT_FOUND).build());
  }

  @POST
  public Response create(@NotNull @Valid Fruit fruit) {
    service.addFruit(fruit);
    return Response.status(Status.CREATED).build();
  }

  @DELETE
  @Path("/{name}")
  public void deleteFruit(@PathParam("name") String name) {
    service.deleteFruit(name);
  }
}
