package scenarios;

import org.junit.Test;
import static io.restassured.RestAssured.given;

public class ServiceHealth {
    private static String baseURL = "http://localhost:3001";

    @Test
    public void t01_verifyServiceHealth() {
        given()
                .contentType("application/json")
                .when()
                .get(baseURL)
                .then()
                .statusCode(200);
    }
}
