package academy.devdojo.controller;

import academy.devdojo.model.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.utils.FileUtils;
import academy.devdojo.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class UserControllerTest {
    private final String URL = "/v1/users";
    @Autowired
    private MockMvc mockMvc;
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
        String responseExpected = fileUtils.readResourceFile("user/get-user-null-firstname-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/users?firstName=Vinicius returns list with found object when first name found")
    void findAll_ReturnsListWithFoundObject_WhenNameFound() throws Exception {
        String responseExpected = fileUtils.readResourceFile("user/get-user-vinicius-firstname-200.json");

        String nameToBeFound = "Vinicius";
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", nameToBeFound))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/users?firstName=NomeQueNaoExiste returns empty list when first name not found")
    void findAll_ReturnsEmptyList_WhenNameNotFound() throws Exception {
        String responseExpected = fileUtils.readResourceFile("user/get-user-nomequenaoexiste-firstname-200.json");

        String randomName = "Nome Que Nao Existe";
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", randomName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}