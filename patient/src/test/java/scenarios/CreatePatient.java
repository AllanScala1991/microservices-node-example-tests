package scenarios;

import com.github.javafaker.Faker;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.Config;
import utils.Patient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreatePatient {
    private static final Config config = new Config();
    private static final Patient patient = new Patient();
    private static final Faker faker = new Faker();
    private static String patientId;
    private final String token = patient.login(config.USERNAME, config.PASSWORD);

    @Test
    public void t01_createPatientSuccessfully() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("name", faker.name().firstName());
        payload.put("email", faker.name().firstName().toLowerCase() + "@gmail.com");
        payload.put("address", "Rua fake, 1888");
        payload.put("consults", "[bfkdlgj8935n8sdfmsdnf4w9n]");
        payload.put("cpf", "88888888888");
        payload.put("exams", "[ns8a9dnas9dna8sdna9snd]");
        payload.put("genrer", "M");
        payload.put("insurance", "has7d9as7dhas87dha9sd");
        payload.put("phone", "4199" + faker.number().digits(7));

        patientId = given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .log().all()
                .body(payload)
                .when()
                .post(config.URL + "/create")
                .then()
                .log().all()
                .statusCode(201)
                .body("data", is(notNullValue()))
                .extract().path("data.id");
    }

    @Test
    public void t02_createPatientWithEmptyPayload() {
        Map<String, String> payload = new HashMap<String, String>();

        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .log().all()
                .body(payload)
                .when()
                .post(config.URL + "/create")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", is("Todos os campos devem ser preenchidos."));
    }

    @Test
    public void t03_createPatientWithDuplicatedCPF() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("name", faker.name().firstName());
        payload.put("email", faker.name().firstName().toLowerCase() + "@gmail.com");
        payload.put("address", "Rua fake, 1888");
        payload.put("consults", "[bfkdlgj8935n8sdfmsdnf4w9n]");
        payload.put("cpf", "88888888888");
        payload.put("exams", "[ns8a9dnas9dna8sdna9snd]");
        payload.put("genrer", "M");
        payload.put("insurance", "has7d9as7dhas87dha9sd");
        payload.put("phone", "4199" + faker.number().digits(7));

        given()
                .headers("Authorization", "Bearer " + token)
                .contentType("application/json")
                .log().all()
                .body(payload)
                .when()
                .post(config.URL + "/create")
                .then()
                .log().all()
                .statusCode(409)
                .body("message", is("Já existe um paciente cadastrado com esse documento."));
    }

    @AfterClass
    public static void afterAll() throws IOException {
        patient.delete(patientId);
    }
}
