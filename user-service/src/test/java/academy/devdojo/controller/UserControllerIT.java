package academy.devdojo.controller;

import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserRepository;
import academy.devdojo.utils.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT extends IntegrationTestConfig {
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

    @Test
    @DisplayName("POST /v1/users returns saved user when successful")
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(6)
    void save_ReturnsSavedUser_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("user/post-request-user-200.json");
        String expectedResponse = fileUtils.readResourceFile("user/post-response-user-201.json");

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .isNotNull()
                .isNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete /v1/users/1 removes user when successful")
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() {
        List<User> users = repository.findByFirstNameIgnoreCase("Marcelo");

        Assertions.assertThat(users)
                .hasSize(1);

        Long idToDelete = users.getFirst().getId();
        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", idToDelete)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("Delete /v1/users/randomId throws NotFoundException when id not exists")
    @Order(8)
    void delete_ThrowsNotFoundException_WhenIdIsNotFound() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/delete-user-by-id-404.json");

        Long randomId = 999L;
        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", randomId)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("PUT /v1/users updates given user when successful")
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Order(9)
    void update_UpdatesGivenUser_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("user/put-request-user-200.json");

        List<User> users = repository.findByFirstNameIgnoreCase("Marcelo");
        User userToUpdate = users.getFirst();

        request = request.replace("1", userToUpdate.getId().toString());

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("PUT /v1/users throws NotFoundException when id not exists")
    @Order(10)
    void update_ThrowsNotFoundException_WhenUserNotExists() throws Exception {
        String request = fileUtils.readResourceFile("user/put-request-user-404.json");
        String expectedResponse = fileUtils.readResourceFile("user/put-user-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @ParameterizedTest
    @DisplayName("POST /v1/users returns bad request when fields are invalid")
    @MethodSource("postUserBadRequestSource")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String fileResponse) throws Exception {
        String request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        String expectedResponse = fileUtils.readResourceFile("user/%s".formatted(fileResponse));

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", "post-response-user-empty-fields-400.json"),
                Arguments.of("post-request-user-blank-fields-400.json", "post-response-user-blank-fields-400.json"),
                Arguments.of("post-request-user-invalid-email-400.json", "post-response-user-invalid-email-400.json")
        );
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT /v1/users returns bad request when fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) throws Exception {
        String request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        String expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", "put-response-user-empty-fields-400.json"),
                Arguments.of("put-request-user-blank-fields-400.json", "put-response-user-blank-fields-400.json")
        );
    }
}