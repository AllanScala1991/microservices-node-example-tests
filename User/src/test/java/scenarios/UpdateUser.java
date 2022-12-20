package scenarios;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UpdateUser {
    private static String token;
    private static String id;
    private static final UserService userService = new UserService();
    private static final String baseURL = "http://localhost:3001";

    @BeforeClass
    public static void beforeAll() throws IOException {
        id = userService.create("name", "test@test.com", "41999999999", "username", "password");
        token = userService.login("username", "password");
    }

    @Test
    public void t01_updateUserSuccessfully() {
        Map<String , String> payload = new HashMap<String, String>();
        payload.put("id", id);
        payload.put("name", "updated name");
        payload.put("email", "updated_email@test.com");
        payload.put("phone", "41888888888");
        payload.put("username", "username_updated");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .put(baseURL)
                .then()
                .statusCode(200)
                .body("data.name", is("updated name"))
                .body("data.email", is("updated_email@test.com"))
                .body("data.phone", is("41888888888"))
                .body("data.username", is("username_updated"));
    }

    @Test
    public void t02_sendEmptyValues() {
        Map<String , String> payload = new HashMap<String, String>();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .put(baseURL)
                .then()
                .statusCode(400)
                .body("message", is("Todos os campos devem ser preenchidos, tente novamente."));
    }

    @Test
    public void t03_sendInvalidID() {
        Map<String , String> payload = new HashMap<String, String>();
        payload.put("id", "000000000");
        payload.put("name", "updated name");
        payload.put("email", "updated_email@test.com");
        payload.put("phone", "41888888888");
        payload.put("username", "username_updated");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .put(baseURL)
                .then()
                .log().all()
                .statusCode(500)
                .body("message.meta.cause", is("Record to update not found."));
    }

    @AfterClass
    public static void afterAll() throws IOException {
        userService.delete(id, "username_updated", "password");
    }
}
