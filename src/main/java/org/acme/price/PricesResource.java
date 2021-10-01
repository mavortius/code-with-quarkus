package org.acme.price;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.reactivestreams.Publisher;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("prices")
public class PricesResource {
  private final Publisher<Double> prices;

  public PricesResource(@Channel("my-data-stream") Publisher<Double> prices) {
    this.prices = prices;
  }

  @GET
  @Path("stream")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public Publisher<Double> stream() {
    return prices;
  }
}
