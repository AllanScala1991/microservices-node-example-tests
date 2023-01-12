package utils;

import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class Patient {
    private static final Config config = new Config();
    private static final Faker faker = new Faker();

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

    public String create() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("name", faker.name().firstName());
        payload.put("email", faker.name().firstName().toLowerCase() + "@gmail.com");
        payload.put("address", "Rua fake, 1888");
        payload.put("consults", "bfkdlgj8935n8sdfmsdnf4w9n");
        payload.put("cpf", faker.number().digits(11));
        payload.put("exams", "ns8a9dnas9dna8sdna9snd");
        payload.put("genrer", "M");
        payload.put("insurance", "has7d9as7dhas87dha9sd");
        payload.put("phone", "4199" + faker.number().digits(7));

        String token = login(config.USERNAME, config.PASSWORD);

        return given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post(config.URL + "/create")
                .then()
                .statusCode(201)
                .extract().path("data.id");
    }
}
