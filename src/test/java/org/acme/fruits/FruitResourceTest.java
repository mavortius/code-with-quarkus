package org.acme.fruits;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.NotFoundException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankOrNullString;

@QuarkusTest
class FruitResourceTest {

  @InjectMock
  FruitService fruitService;

  @Test
  void fruitListOk() {
    Mockito.when(this.fruitService.getFruits())
            .thenReturn(List.of(new Fruit("Apple", "Winter fruit")));

    given()
            .when().get("/fruits")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body(
                    "$.size()", is(1),
                    "[0].name", is("Apple"),
                    "[0].description", is("Winter fruit")
            );

    Mockito.verify(this.fruitService).getFruits();
    Mockito.verifyNoMoreInteractions(this.fruitService);
  }

  @Test
  void getFruitFound() {
    Mockito.when(fruitService.getFruit("apple"))
            .thenReturn(Uni.createFrom().item(new Fruit("Apple", "Winter fruit")));

    given()
            .when().get("/fruits/apple")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("name", is("Apple"),
                    "description", is("Winter fruit"));

    Mockito.verify(fruitService).getFruit("apple");
    Mockito.verifyNoMoreInteractions(fruitService);
  }

  @Test
  void getFruitNotFound() {
    Mockito.when(fruitService.getFruit("pear")).thenReturn(Uni.createFrom().nullItem());

    given()
            .when().get("/fruits/pear")
            .then().statusCode(404)
            .body(blankOrNullString());

    Mockito.verify(fruitService).getFruit("pear");
    Mockito.verifyNoMoreInteractions(fruitService);
  }

  @Test
  void addFruit() {
    given()
            .contentType(ContentType.JSON)
            .body(new Fruit("Pear", "Refreshing fruit"))
            .when().post("/fruits")
            .then()
            .statusCode(201)
            .body(blankOrNullString());

    Mockito.verify(fruitService).addFruit(Mockito.any(Fruit.class));
    Mockito.verifyNoMoreInteractions(fruitService);
  }

  @Test
  void addInvalidFruit() {
    given()
            .contentType(ContentType.JSON)
            .body("{\"description\":\"Description\"}")
            .when().post("/fruits")
            .then().statusCode(400);

    Mockito.verifyNoInteractions(fruitService);
  }

  @Test
  void deleteFruit() {
    given()
            .when().delete("/fruits/apple")
            .then().statusCode(204);

    Mockito.verify(fruitService).deleteFruit("apple");
    Mockito.verifyNoMoreInteractions(fruitService);
  }

  @Test
  void deleteNotFoundFruit() {
    Mockito.doThrow(new NotFoundException("Fruit not found")).when(fruitService).deleteFruit("cashew");

    given()
            .when().delete("/fruits/cashew")
            .then().statusCode(404);

    Mockito.verify(fruitService).deleteFruit("cashew");
    Mockito.verifyNoMoreInteractions(fruitService);
  }
}
