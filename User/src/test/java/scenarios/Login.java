package scenarios;

import com.github.javafaker.Faker;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Login {
    private static String baseURL = "http://localhost:3001";
    private static Faker faker = new Faker();
    private static String username = faker.name().firstName() + faker.number().digits(2);
    private static String password = faker.number().digits(8);
    private static String id;

    @BeforeClass
    public static void beforeAll() throws IOException {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("name", faker.name().firstName().toLowerCase());
        payload.put("email",  faker.name().firstName().toLowerCase() + "@teste.com");
        payload.put("phone", "41999999999");
        payload.put("username", username);
        payload.put("password", password);

        id = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(baseURL + "/create")
                .then()
                .statusCode(201)
                .extract().path("data.id");
    }

    @Test
    public void t01_loginWithSuccess() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("username", username);
        payload.put("password", password);

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(baseURL + "/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    public void t02_loginWithInvalidUsername() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("username", "invalid");
        payload.put("password", password);

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(baseURL + "/login")
                .then()
                .statusCode(401)
                .body("message", is("Usuário ou Senha incorretos, tente novamente."));
    }

    @Test
    public void t03_loginWithInvalidPassword() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("username", username);
        payload.put("password", "111111");

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(baseURL + "/login")
                .then()
                .statusCode(401)
                .body("message", is("Usuário ou Senha incorretos, tente novamente."));
    }

    @Test
    public void t04_loginWithEmptyValues() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("username", "");
        payload.put("password", "");

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(baseURL + "/login")
                .then()
                .statusCode(401)
                .body("message", is("Usuário ou Senha incorretos, tente novamente."));
    }
}
