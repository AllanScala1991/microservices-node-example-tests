package utils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Patient {
    private static final Config config = new Config();

    public String login(String username, String password) {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("username", username);
        payload.put("password", password);

        return given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(config.LOGIN)
                .then()
                .statusCode(200)
                .extract().path("token");
    }

    public void delete(String id) {
        String token = login(config.USERNAME, config.PASSWORD);
        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(config.URL + "/" + id)
                .then()
                .statusCode(200);
    }
}
