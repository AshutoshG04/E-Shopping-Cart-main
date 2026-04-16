// Path: src/test/java/tests/api/ProductApiTests.java
package tests.api;

import base.BaseApiTest;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import utils.AllureAttach;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Verto Backend")
@Feature("Products API")
public class ProductApiTests extends BaseApiTest {

    @Test(description = "GET /products returns list with expected items")
    public void getProducts_shouldReturnList() {
        String body =
                given().accept(ContentType.JSON)
                        .when().get("/products")
                        .then().statusCode(200)
                        .contentType(ContentType.JSON)
                        // CORRECTED: Check for hardcoded size and a known product ID
                        .body("size()", is(5))
                        .body("id", hasItem("p_001")) // Check that "p_001" is in the list of IDs
                        .extract().asString();

        AllureAttach.json("GET /products response", body);
    }

    // REMOVED: The test for GET /products/{id} was invalid,
    // as that specific endpoint is not defined in ProductController.java.
}