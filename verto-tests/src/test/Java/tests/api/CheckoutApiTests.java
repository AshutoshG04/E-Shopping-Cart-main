// Path: src/test/java/tests/api/CheckoutApiTests.java
package tests.api;

import base.BaseApiTest;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import utils.AllureAttach;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Verto Backend")
@Feature("Checkout API")
public class CheckoutApiTests extends BaseApiTest {

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "POST /checkout with valid payload")
    public void checkout_validPayload_shouldCreateOrder() {
        // CORRECTED: Payload matches CheckoutRequest.java
        // "productId" is a string (e.g., "p_001")
        // "user" object (not "customer")
        // No "address" or "payment" fields
        String payload = """
            {
              "items":[{"productId":"p_001","quantity":2}],
              "user":{"name":"Anuj API Test","email":"api@verto.com"}
            }
            """;

        AllureAttach.json("POST /checkout request", payload);

        String body =
                given().contentType(ContentType.JSON)
                        .body(payload)
                        .when().post("/checkout")
                        .then()
                        // CORRECTED: Assertions match the actual API response
                        .statusCode(200) // API returns 200 OK
                        .body("success", is(true))
                        .body("orderId", is(notNullValue()))
                        .body("totalCents", is(10000)) // 2 * 5000 (price of p_001)
                        .extract().asString();

        AllureAttach.json("POST /checkout response", body);
    }

    @Test(description = "POST /checkout with invalid productId")
    public void checkout_invalidProduct_shouldReturn422() {
        // Payload with an invalid (non-existent) product ID
        String payload = """
            {
              "items":[{"productId":"p_999","quantity":1}],
              "user":{"name":"Anuj API Test","email":"api@verto.com"}
            }
            """;

        AllureAttach.json("POST /checkout invalid request", payload);

        String body =
                given().contentType(ContentType.JSON)
                        .body(payload)
                        .when().post("/checkout")
                        .then()
                        // CORRECTED: Assertions match the 422 Unprocessable Entity response
                        .statusCode(422)
                        .body("error", is("Invalid productId(s)"))
                        .body("invalidIds[0]", is("p_999"))
                        .extract().asString();

        AllureAttach.json("POST /checkout 422 response", body);
    }
}