package org.acme.price;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import org.jboss.resteasy.reactive.client.impl.MultiInvoker;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class PricesResourceTest {

  @TestHTTPEndpoint(PricesResource.class)
  @TestHTTPResource("/stream")
  URI uri;

  @Test
  void sseEventStream() {
    List<Double> received = ClientBuilder.newClient()
            .target(uri)
            .request(MediaType.SERVER_SENT_EVENTS)
            .rx(MultiInvoker.class)
            .get(Double.class)
            .select().first(3)
            .subscribe().withSubscriber(AssertSubscriber.create(3))
            .assertSubscribed()
            .awaitItems(3, Duration.ofSeconds(20))
            .assertCompleted()
            .getItems();

    assertThat(received)
            .hasSize(3)
            .allMatch(value -> (value >= 0) && (value < 100));
  }
}