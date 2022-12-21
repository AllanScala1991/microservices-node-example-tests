package scenarios;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.github.javafaker.Faker;
import org.junit.runners.MethodSorters;
import utils.UserService;

import static io.restassured.RestAssured.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateUser {
    private static String baseURL = "http://localhost:3001";
    private static Faker faker = new Faker();
    private static String name = faker.name().firstName();
    private static String email =  name.toLowerCase() + "@teste.com";
    private static String phone = "41999999999";
    private static String username = faker.name().firstName().toLowerCase() + faker.number().digits(2);
    private static String password = faker.number().digits(8);
    private static String id;
    private static final UserService userService = new UserService();

    @Test
    public void t01_createUserSuccessfully() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("name", name);
        payload.put("email", email);
        payload.put("phone", phone);
        payload.put("username", username);
        payload.put("password", password);

        id = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(baseURL + "/create")
                .then()
                .statusCode(201)
                .body("data.name", is(name))
                .body("data.email", is(name.toLowerCase() + "@teste.com"))
                .extract().path("data.id");
    }

    @Test
    public void t02_createUserWithInvalidEmailFormat() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("name", name);
        payload.put("email", "invalid_email.com");
        payload.put("phone", phone);
        payload.put("username", username);
        payload.put("password", password);


        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(baseURL + "/create")
                .then()
                .statusCode(400)
                .body("message", is("Formato do email inválido."));
    }

    @Test
    public void t03_createExistsUser() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("name", name);
        payload.put("email", email);
        payload.put("phone", phone);
        payload.put("username", username);
        payload.put("password", password);


        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(baseURL + "/create")
                .then()
                .statusCode(409)
                .body("message", is("Usuário já cadastrado, recupere a senha ou tente novamente."));
    }

    @AfterClass
    public static void afterAll() throws IOException {
        userService.delete(id, username, password);
    }
}
