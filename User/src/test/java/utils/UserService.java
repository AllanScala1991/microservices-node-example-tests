package utils;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class UserService {
    public String create(String name, String email, String phone, String username, String password) {
        String id;

        Map<String, String> payload = new HashMap<String, String>();
        payload.put("name", name);
        payload.put("email",  email);
        payload.put("phone", phone);
        payload.put("username", username);
        payload.put("password", password);

        id = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("http://localhost:3001/create")
                .then()
                .statusCode(201)
                .extract().path("data.id");

        return  id;
    }

    public String login(String username, String password) {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("username", username);
        payload.put("password", password);

        return given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("http://localhost:3001/login")
                .then()
                .statusCode(200)
                .extract().path("token");
    }

    public void delete(String id, String username, String password) {
        String token = login(username, password);
        given()
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .contentType("application/json")
                .when()
                .delete("http://localhost:3001/" + id)
                .then()
                .statusCode(200);
    }
}
