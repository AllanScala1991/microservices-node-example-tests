package scenarios;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.Config;
import utils.Patient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UpdatePatient {

    private static final Config config = new Config();
    private static final Patient patient = new Patient();
    private static String token;

    private static String id;

    @BeforeClass
    public static void beforeAll() throws IOException {
        token = patient.login(config.USERNAME, config.PASSWORD);
        id = patient.create();
    }

    @Test
    public void t01_UpdatedPatientSuccessfully() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("id", id);
        payload.put("name", "Updated Name");
        payload.put("email", "updated_email@gmail.com");
        payload.put("address", "Updated Address");
        payload.put("consults", "234234v2342y3423h1");
        payload.put("exams", "5675p675i6756n7567j");
        payload.put("genrer", "M");
        payload.put("insurance", "678nk67j67867jk8n67kj8");
        payload.put("phone", "41966666666");

        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .put(config.URL)
                .then()
                .statusCode(200)
                .body("data.name", is("Updated Name"))
                .body("data.email", is("updated_email@gmail.com"));
    }

    @Test
    public void t02_UpdatedPatientWithNotSendName() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("id", id);
        payload.put("email", "updated_email@gmail.com");
        payload.put("address", "Updated Address");
        payload.put("consults", "234234v2342y3423h1");
        payload.put("exams", "5675p675i6756n7567j");
        payload.put("genrer", "M");
        payload.put("insurance", "678nk67j67867jk8n67kj8");
        payload.put("phone", "41966666666");

        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .put(config.URL)
                .then()
                .statusCode(400)
                .body("message", is("Todos os campos devem ser preenchidos."));
    }

    @Test
    public void t03_UpdatedPatientWithSendInvalidId() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("id", "invalidID");
        payload.put("name", "Updated Name");
        payload.put("email", "updated_email@gmail.com");
        payload.put("address", "Updated Address");
        payload.put("consults", "234234v2342y3423h1");
        payload.put("exams", "5675p675i6756n7567j");
        payload.put("genrer", "M");
        payload.put("insurance", "678nk67j67867jk8n67kj8");
        payload.put("phone", "41966666666");

        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .put(config.URL)
                .then()
                .statusCode(500)
                .body("message.meta.cause", is("Record to update not found."));
    }

    @AfterClass
    public static void afterAll() {
        patient.delete(id);
    }
}
