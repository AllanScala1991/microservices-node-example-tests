package scenarios;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.UserService;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeleteUser {
    private static String id;
    private static String token;
    private static final String baseURL = "http://localhost:3001";
    private static final UserService userService = new UserService();

    @BeforeClass
    public static void beforeAll() throws IOException {
        id = userService.create("name", "test@test.com", "41999999999", "username", "password");
        token = userService.login( "username", "password");
    }

    @Test
    public void t01_deletedUserSuccessfully() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(baseURL + "/" + id)
                .then()
                .statusCode(200)
                .body("message", is("Usuário deletado com sucesso."));
    }

    @Test
    public void t02_sendInvalidID() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(baseURL + "/" + "0000000")
                .then()
                .statusCode(500)
                .body("message.meta.cause", is("Record to delete does not exist."));
    }

    @Test
    public void t03_sendEmptyID() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(baseURL + "/" + "")
                .then()
                .statusCode(404);
    }
}
