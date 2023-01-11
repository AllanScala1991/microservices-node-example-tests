package scenarios;

import org.junit.Test;
import utils.Config;

import static io.restassured.RestAssured.given;

public class ServiceHealth {
    private static final Config config = new Config();
    @Test
    public void verifyServiceHealth() {
        String baseURL = config.URL;

        given()
                .contentType("application/json")
                .when()
                .get(baseURL + "/health")
                .then()
                .statusCode(200);
    }
}
