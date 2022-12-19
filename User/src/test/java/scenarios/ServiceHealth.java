package scenarios;

import org.junit.Test;
import static io.restassured.RestAssured.given;

public class ServiceHealth {

    @Test
    public void t01_verifyServiceHealth() {
        String baseURL = "http://localhost:3001";
        given()
                .contentType("application/json")
                .when()
                .get(baseURL)
                .then()
                .statusCode(200);
    }
}
