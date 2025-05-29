package academy.devdojo.controller;

import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserRepository;
import academy.devdojo.utils.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest extends IntegrationTestConfig {
    private final String URL = "/v1/users";
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private UserRepository repository;
    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("GET /v1/users returns all users when first name is null")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenNameIsNull() throws IOException {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-null-firstname-200.json");

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .isNotNull()
                .and(users -> {
                    users.node("[0].id").asNumber().isPositive();
                    users.node("[1].id").asNumber().isPositive();
                    users.node("[2].id").asNumber().isPositive();
                });

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(2)
    @DisplayName("GET /v1/users?firstName=Vinicius returns list with found object when first name found")
    void findAll_ReturnsListWithFoundObject_WhenNameFound() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-vinicius-firstname-200.json");

        String nameToSearch = "vinicius";
        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("firstName", nameToSearch)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET /v1/users?firstName=NomeQueNaoExiste returns empty list when first name not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameNotFound() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-nomequenaoexiste-firstname-200.json");

        String randomFirstName = "Nome Que Nao Existe";
        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("firstName", randomFirstName)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET /v1/users/1 returns user found when id exists")
    @Order(4)
    void findById_ReturnsUserFound_WhenIdExists() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-200.json");

        List<User> users = repository.findByFirstNameIgnoreCase("Marcelo");

        Assertions.assertThat(users)
                .hasSize(1);

        Long idToSearch = users.getFirst().getId();
        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", idToSearch)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET /v1/users/999 throws NotFoundException when id not exists")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenIdNotExists() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-404.json");

        Long randomId = 999L;

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", randomId)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }
}