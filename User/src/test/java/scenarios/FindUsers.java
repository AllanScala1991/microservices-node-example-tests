package scenarios;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.UserService;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FindUsers {
    private static final String baseURL = "http://localhost:3001";
    private static String id;

    private static final UserService userService = new UserService();
    @BeforeClass
    public static void beforeAll() throws IOException {
        id = userService.create("Test Name","test@test.com",  "41999999999",  "username", "password");
    }

    @Test
    public void t01_findUserByID() {
        given()
                .contentType("application/json")
                .when()
                .get(baseURL + "/" + id)
                .then()
                .statusCode(200)
                .body("data.name", is("Test Name"))
                .body("data.email", is("test@test.com"));
    }

    @Test
    public void t02_findUserByInvalidID() {
        given()
                .contentType("application/json")
                .when()
                .get(baseURL + "/" + "invalid")
                .then()
                .statusCode(200)
                .body("data", is(nullValue()));
    }

    @Test
    public void t03_findAllUsers() {
        String secondUserID = userService.create("Test Name","test2@test.com",  "41999999999",  "username2", "password");

        given()
                .contentType("application/json")
                .when()
                .get(baseURL + "/")
                .then()
                .statusCode(200)
                .body("data", is(hasSize(2)))
                .extract().path("data");

        userService.delete(secondUserID, "username2", "password");
    }

    @AfterClass
    public static void afterAll() throws  IOException {
        userService.delete(id, "username", "password");
    }

}
