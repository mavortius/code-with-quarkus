package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {
  @InjectMock
  GreetingService greetingService;

  @Test
  void testHelloEndpoint() {
    Mockito.when(greetingService.getGreeting()).thenReturn("Quarkus");

    given()
            .when().get("/hello")
            .then()
            .statusCode(200)
            .body(is("Quarkus"));

    Mockito.verify(greetingService).getGreeting();
    Mockito.verifyNoMoreInteractions(greetingService);
  }

  @Test
  void testGreeting() {
    Mockito.when(greetingService.consume("events")).thenReturn("EVENTS");

    given()
            .when().get("/async/events")
            .then()
            .statusCode(200)
            .contentType(ContentType.TEXT)
            .body(is("EVENTS"));

    Mockito.verify(greetingService).consume("events");
    Mockito.verifyNoMoreInteractions(greetingService);
  }
}