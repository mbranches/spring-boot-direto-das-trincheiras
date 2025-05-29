package academy.devdojo.controller;

import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.utils.FileUtils;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileControllerIntegrationTest extends IntegrationTestConfig {
    private static final String URL = "/v1/profiles";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private FileUtils fileUtils;

    @Test
    @DisplayName("GET /v1/profiles returns all profiles when first name is null")
    @Sql(value = "/sql/profile/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/profile/clean_profile.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenNameIsNull() {
        ParameterizedTypeReference<List<ProfileGetResponse>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<ProfileGetResponse>> response = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotEmpty().doesNotContainNull();

        response.getBody().forEach(p -> Assertions.assertThat(p).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("GET /v1/profiles returns an empty list when nothing is found")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenNothingIsNotFound() {
        ParameterizedTypeReference<List<ProfileGetResponse>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<ProfileGetResponse>> response = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("POST /v1/profiles returns saved profile when successful")
    @Order(3)
    void save_ReturnsSavedProfile_WhenSuccessful() throws IOException {
        String request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        HttpEntity<String> profileHttpEntity = buildHttpRequest(request);

        ResponseEntity<ProfilePostResponse> response = testRestTemplate.exchange(URL, HttpMethod.POST, profileHttpEntity, ProfilePostResponse.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("save returns BadRequestException when the fields are invalid")
    @Order(4)
    void save_ReturnsBadRequestException_WhenTheFieldsAreInvalid(String requestFile, String responseFile) throws IOException {
        String request = fileUtils.readResourceFile("profile/%s".formatted(requestFile));
        String expectedResponse = fileUtils.readResourceFile("profile/%s".formatted(responseFile));

        HttpEntity<String> httpEntityRequest = buildHttpRequest(request);

        ResponseEntity<String> response  = testRestTemplate.exchange(URL, HttpMethod.POST, httpEntityRequest, String.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        JsonAssertions.assertThatJson(response.getBody())
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);
    }


    private static Stream<Arguments> postProfileBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-profile-empty-fields-400.json", "post-response-profile-empty-fields-400.json"),
                Arguments.of("post-request-profile-blank-fields-400.json", "post-response-profile-blank-fields-400.json")
        );
    }

        private static HttpEntity<String> buildHttpRequest(String json) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(json, httpHeaders);
    }
}

