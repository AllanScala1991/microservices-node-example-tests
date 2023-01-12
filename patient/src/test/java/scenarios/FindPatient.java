package scenarios;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.Config;
import utils.Patient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FindPatient {
    private static final Config config = new Config();
    private static final Patient patient = new Patient();
    private static final List<String> patients = new ArrayList<>();
    private static String token;

    @BeforeClass
    public static void beforeAll() {
        String firstPatient = patient.create();
        patients.add(firstPatient);

        String secondPatient = patient.create();
        patients.add(secondPatient);

        token = patient.login(config.USERNAME, config.PASSWORD);
    }

    @Test
    public void t01_findAllPatients() {
        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .get(config.URL)
                .then()
                .statusCode(200)
                .body("data", is(notNullValue()))
                .body("data.data.size()", is(2));
    }

    @Test
    public void t02_findPatientById() {
        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .get(config.URL + "/" + patients.get(0))
                .then()
                .statusCode(200)
                .body("data", is(notNullValue()));
    }

    @Test
    public void t03_findPatientByInvalidId() {
        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .get(config.URL + "/" + "invalidID")
                .then()
                .statusCode(404)
                .body("message", is("Nenhum paciente localizado."));
    }

    @AfterClass
    public static void afterAll()  throws IOException {
        for(String patientId : patients) {
            patient.delete(patientId);
        }
    }


}
