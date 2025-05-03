package academy.devdojo.controller;

import academy.devdojo.model.Profile;
import academy.devdojo.repository.ProfileRepository;
import academy.devdojo.repository.UserRepository;
import academy.devdojo.utils.FileUtils;
import academy.devdojo.utils.ProfileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class ProfileControllerTest {
    private final String URL = "/v1/profiles";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProfileRepository repository;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProfileUtils profileUtils;
    private List<Profile> profileList;

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();

        BDDMockito.when(repository.findAll()).thenReturn(profileList);
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/profiles returns all profiles when first name is null")
    void findAll_ReturnsAllProfiles_WhenNameIsNull() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("profile/get-profiles-200.json");

        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("POST /v1/profiles returns saved profile when successful")
    @Order(2)
    void save_ReturnsSavedProfile_WhenSuccessful() throws Exception {
        Profile profileSaved = profileUtils.newProfileSaved();

        BDDMockito.when(repository.save(ArgumentMatchers.any(Profile.class))).thenReturn(profileSaved);

        String request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        String response = fileUtils.readResourceFile("profile/post-response-profile-201.json");
        mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST /v1/profiles returns bad request when fields are invalid")
    @Order(3)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> expectedErrors) throws Exception {
        String request = fileUtils.readResourceFile("profile/%s".formatted(fileName));
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post(URL)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException.getMessage())
                .isNotNull()
                .contains(expectedErrors);
    }

    private static Stream<Arguments> postProfileBadRequestSource() {
        List<String> allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-profile-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-profile-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        String nameRequiredError = "The field 'name' is required";
        String descriptionRequiredError = "The field 'description' is required";

        return new ArrayList<>(List.of(nameRequiredError, descriptionRequiredError));
    }
}