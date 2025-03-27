package academy.devdojo.controller;

import academy.devdojo.model.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.repository.UserHardCodedRepository;
import academy.devdojo.utils.FileUtils;
import academy.devdojo.utils.UserUtils;
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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class UserControllerTest {
    private final String URL = "/v1/users";
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private UserHardCodedRepository repository;
    @MockBean
    private UserData userData;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private UserUtils userUtils;
    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();

        BDDMockito.when(userData.getUsers()).thenReturn(userList);
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/users returns all users when first name is null")
    void findAll_ReturnsAllUsers_WhenNameIsNull() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-null-firstname-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/users?firstName=Vinicius returns list with found object when first name found")
    void findAll_ReturnsListWithFoundObject_WhenNameFound() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-vinicius-firstname-200.json");

        String nameToBeFound = "Vinicius";
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", nameToBeFound))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/users?firstName=NomeQueNaoExiste returns empty list when first name not found")
    void findAll_ReturnsEmptyList_WhenNameNotFound() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-nomequenaoexiste-firstname-200.json");

        String randomName = "Nome Que Nao Existe";
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", randomName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("GET /v1/users/1 returns user found when id exists")
    void findById_ReturnsUserFound_WhenIdExists() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-200.json");

        Long idToBeSearched = userList.get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", idToBeSearched))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(5)
    @DisplayName("GET /v1/users/randomId throws ResponseStatusException when id not exists")
    void findById_ThrowsResponseStatusException_WhenIdNotExists() throws Exception {
        Long randomId = 12192L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", randomId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not Found"));
    }

    @Test
    @DisplayName("POST /v1/users returns saved user when successful")
    @Order(6)
    void save_ReturnsSavedUser_WhenSuccessful() throws Exception {
        User userSaved = userUtils.newUserToBeSaved();
        BDDMockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(userSaved);

        String request = fileUtils.readResourceFile("user/post-request-user-200.json");
        String response = fileUtils.readResourceFile("user/post-response-user-201.json");
        mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("PUT /v1/users updates given user when successful")
    @Order(7)
    void update_UpdatesGivenUser_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("user/put-request-user-200.json");

        mockMvc.perform(
                MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("PUT /v1/users throws ResponseStatusException when id not exists")
    void update_ThrowsResponseStatusException_WhenUserNotExists() throws Exception {
        String request = fileUtils.readResourceFile("user/put-request-user-404.json");

        mockMvc.perform(
                    MockMvcRequestBuilders.put(URL)
                            .content(request)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not Found"));
    }

    @Test
    @DisplayName("Delete /v1/users/1 removes user when successful")
    @Order(9)
    void delete_RemovesUser_WhenSuccessful() throws Exception {
        User userToBeDeleted = userList.get(0);
        Long idToBeDeleted = userToBeDeleted.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", idToBeDeleted))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("Delete /v1/users/randomId throws ResponseStatusException when id not exists")
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception {
        long randomId = 12121L;
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", randomId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not Found"));
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST /v1/users returns bad request when fields are invalid")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> expectedErrors) throws Exception {
        User userSaved = userUtils.newUserToBeSaved();
        BDDMockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(userSaved);

        String request = fileUtils.readResourceFile("user/%s".formatted(fileName));
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

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT /v1/users returns bad request when fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> expectedErrors) throws Exception {
        User userSaved = userUtils.newUserToBeSaved();
        BDDMockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(userSaved);

        String request = fileUtils.readResourceFile("user/%s".formatted(fileName));
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put(URL)
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

    private static Stream<Arguments> postUserBadRequestSource() {
        List<String> allRequiredErrors = allRequiredErrors();
        List<String> emailInvalidErrosList = emailInvalidError();

        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-blank-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-invalid-email-400.json", emailInvalidErrosList)
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        String idNotNullError = "The field 'id' cannot be null";

        List<String> allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add(idNotNullError);
        List<String> emailInvalidErrosList = emailInvalidError();

        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-blank-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-invalid-email-400.json", emailInvalidErrosList)
        );
    }

    private static List<String> allRequiredErrors() {
        String firstNameRequiredError = "The field 'firstName' is required";
        String lastNameRequiredError = "The field 'lastName' is required";
        String emailRequiredError = "The field 'email' is required";

        return new ArrayList<>(List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError));
    }

    private static List<String> emailInvalidError() {
        String emailInvalidError = "email is not valid";

        return Collections.singletonList(emailInvalidError);
    }
}