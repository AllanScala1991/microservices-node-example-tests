package scenarios;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.Config;
import utils.Patient;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeletePatient {

    private static final Config config = new Config();
    private static final Patient patient = new Patient();
    private static String token;
    private static String id;

    @BeforeClass
    public static void beforeAll() {
        token = patient.login(config.USERNAME, config.PASSWORD);
        id = patient.create();
    }

    @Test
    public void t01_deletePatientSuccessfully() {
        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(config.URL + "/" + id)
                .then()
                .statusCode(200)
                .body("message", is("Paciente deletado com sucesso."));
    }

    @Test
    public void t02_deletePatientWithInvalidId() {
        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(config.URL + "/" + "invalid")
                .then()
                .statusCode(500)
                .body("message.meta.cause", is("Record to delete does not exist."));
    }
}
