package academy.devdojo.anime;

import academy.devdojo.model.Anime;
import academy.devdojo.utils.FileUtils;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = AnimeController.class)
@ComponentScan(basePackages = "academy.devdojo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnimeControllerTest {
    private final String URL = "/v1/animes";
    @MockBean
    private AnimeRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private AnimeUtils animeUtils;
    private List<Anime> animeList;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/animes returns all animes when name is null")
    void findAll_ReturnsAllAnimes_WhenNameIsNull() throws Exception {
        String responseExpected = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/animes?name=Naruto returns list with found object when name found")
    void findAll_ReturnsListWithFoundObject_WhenNameFound() throws Exception {
        String responseExpected = fileUtils.readResourceFile("anime/get-anime-naruto-name-200.json");
        String nameToBeFound = "Naruto";
        BDDMockito.when(repository.findAllByNameContaining(nameToBeFound)).thenReturn(List.of(animeList.get(1)));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", nameToBeFound))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/animes?name=NomeQueNaoExiste returns empty list when name not found")
    void findAll_ReturnsEmptyList_WhenNameNotFound() throws Exception {
        String responseExpected = fileUtils.readResourceFile("anime/get-anime-nomequenaoexiste-name-200.json");

        String randomName = "Nome Que Nao Existe";
        BDDMockito.when(repository.findAllByNameContaining(randomName)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", randomName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("GET /v1/animes/1 returns found anime when successful")
    void findById_ReturnsFoundAnime_WhenSuccessful() throws Exception{
        String responseExpected = fileUtils.readResourceFile("anime/get-anime-by-id-200.json");

        Anime expectedAnime = this.animeList.get(1);
        Long expectedAnimeId = expectedAnime.getId();

        BDDMockito.when(repository.findById(expectedAnimeId)).thenReturn(Optional.of(expectedAnime));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", expectedAnimeId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(5)
    @DisplayName("GET /v1/animes/99 throws NotFoundException when id is not found")
    void findByIdOrThrowNotFound_ThrowsNotFoundException_WhenIdIsNotFound() throws Exception {
        Long randomId = 13321910L;

        String expectedResponse = fileUtils.readResourceFile("anime/get-anime-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", randomId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(6)
    @DisplayName("POST /v1/animes returns created anime when successful")
    void save_ReturnsCreatedAnime_WhenSuccessful() throws Exception {
        Anime animeSaved = animeUtils.newAnimeToSave();
        BDDMockito.when(repository.save(ArgumentMatchers.any(Anime.class))).thenReturn(animeSaved);

        String request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
        String responseExpected = fileUtils.readResourceFile("anime/post-response-anime-201.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(7)
    @DisplayName("DELETE /v1/animes/1 remove anime when successful")
    void delete_RemoveAnime_WhenSuccessful() throws Exception {
        Anime animeToBeDeleted = this.animeList.getFirst();
        Long animeToBeDeletedId = animeToBeDeleted.getId();

        BDDMockito.when(repository.findById(animeToBeDeletedId)).thenReturn(Optional.of(animeToBeDeleted));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", animeToBeDeletedId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("DELETE /v1/animes/99 throws NotFoundException when id is not found")
    void delete_ThrowsNotFoundException_WhenIdIsNotFound() throws Exception {
        Long randomId = 99L;

        String expectedResponse = fileUtils.readResourceFile("anime/delete-anime-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", randomId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    @Order(9)
    @DisplayName("UPDATE /v1/animes updates anime when successful")
    void update_UpdatesAnime_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("anime/put-request-anime-200.json");
        Anime animeToUpdate = animeList.getFirst();
        Long idToUpdate = animeToUpdate.getId();

        BDDMockito.when(repository.findById(idToUpdate)).thenReturn(Optional.of(animeToUpdate));

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("UPDATE /v1/animes throws NotFoundException when id is not found")
    void update_ThrowsNotFoundException_WhenIdIsNotFound() throws Exception {
        String request = fileUtils.readResourceFile("anime/put-request-anime-404.json");

        String expectedResponse = fileUtils.readResourceFile("anime/put-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @DisplayName("save returns BadRequest when the fields are invalid")
    @Order(11)
    void save_ReturnsBadRequest_WhenTheFieldsAreInvalid(String fileName, List<String> expectedErrors) throws Exception {
        String request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post(URL)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
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
    @MethodSource("putAnimeBadRequestSource")
    @DisplayName("update returns BadRequest when the fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenTheFieldsAreInvalid(String fileName, List<String> expectedErrors) throws Exception {
        String request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put(URL)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException.getMessage())
                .isNotNull()
                .contains(expectedErrors);
    }

    private static List<String> nameRequiredError() {
        String errorMessage = "The field 'name' is required.";
        return new ArrayList<>(Collections.singletonList(errorMessage));
    }

    private static Stream<Arguments> postAnimeBadRequestSource() {
        return Stream.of(
            Arguments.of("post-request-anime-empty-fields-404.json", nameRequiredError()),
            Arguments.of("post-request-anime-blank-fields-404.json", nameRequiredError())
        );
    }

    private static Stream<Arguments> putAnimeBadRequestSource() {
        String idNotNullError = "The field 'id' cannot be null.";

        List<String> allErrors = nameRequiredError();
        allErrors.add(idNotNullError);

        return Stream.of(
            Arguments.of("put-request-anime-empty-fields-404.json", allErrors),
            Arguments.of("put-request-anime-blank-fields-404.json", allErrors)
        );
    }
}
