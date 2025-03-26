package academy.devdojo.controller;

import academy.devdojo.model.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.repository.UserHardCodedRepository;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.utils.FileUtils;
import academy.devdojo.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.List;

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
    @DisplayName("save returns saved user when successful")
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
}