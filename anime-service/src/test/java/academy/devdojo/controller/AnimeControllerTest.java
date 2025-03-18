package academy.devdojo.controller;

import academy.devdojo.mapper.AnimeMapperImpl;
import academy.devdojo.model.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
import academy.devdojo.service.AnimeService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = AnimeController.class)
@Import({AnimeMapperImpl.class, AnimeService.class, AnimeHardCodedRepository.class, AnimeData.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeData animeData;
    @SpyBean
    private AnimeHardCodedRepository repository;
    @Autowired
    private ResourceLoader resourceLoader;

    private final List<Anime> ANIMES_LIST = new ArrayList<>();

        @BeforeEach
        void init() {
            Anime anime1 = new Anime(1L, "Attack on Titan");
            Anime anime2 = new Anime(2L, "Naruto");
            Anime anime3 = new Anime(3L, "Demon Slayer");
            ANIMES_LIST.addAll(List.of(anime1, anime2, anime3));

            BDDMockito.when(animeData.getANIMES()).thenReturn(ANIMES_LIST);
        }

    @Test
    @Order(1)
    @DisplayName("GET /v1/animes returns all animes when name is null")
    void findAll_ReturnsAllAnimes_WhenNameIsNull() throws Exception {
        String responseExpected = readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/animes?name=Naruto returns list with found object when name found")
    void findAll_ReturnsListWithFoundObject_WhenNameFound() throws Exception {
        String responseExpected = readResourceFile("anime/get-anime-naruto-name-200.json");

        String nameToBeFound = "Naruto";
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", nameToBeFound))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/animes?name=NomeQueNaoExiste returns empty list when name not found")
    void findAll_ReturnsEmptyList_WhenNameNotFound() throws Exception {
        String responseExpected = readResourceFile("anime/get-anime-nomequenaoexiste-name-200.json");

        String randomName = "Nome Que Nao Existe";
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", randomName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("GET /v1/animes/1 returns found anime when successful")
    void findById_ReturnsFoundAnime_WhenSuccessful() throws Exception{
        String responseExpected = readResourceFile("anime/get-anime-by-id-200.json");

        Anime expectedAnime = this.ANIMES_LIST.get(1);
        Long expectedAnimeId = expectedAnime.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", expectedAnimeId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(5)
    @DisplayName("GET /v1/animes/99 throws ResponseStatusException when id is not found")
    void findByIdOrThrowNotFound_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception {
        Long randomId = 13321910L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", randomId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    @Test
    @Order(6)
    @DisplayName("POST /v1/animes returns created anime when successful")
    void save_ReturnsCreatedAnime_WhenSuccessful() throws Exception {
        Anime animeSaved = Anime.builder().id(100L).name("novo anime").build();
        BDDMockito.when(repository.save(ArgumentMatchers.any(Anime.class))).thenReturn(animeSaved);

        String request = readResourceFile("anime/post-request-anime-200.json");
        String responseExpected = readResourceFile("anime/post-response-anime-201.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/animes")
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
        Anime animeToBeDeleted = this.ANIMES_LIST.get(0);
        Long animeToBeDeletedId = animeToBeDeleted.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", animeToBeDeletedId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("DELETE /v1/animes/99 throws ResponseStatusException when id is not found")
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception {
        Long randomId = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", randomId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    @Test
    @Order(9)
    @DisplayName("UPDATE /v1/animes updates anime when successful")
    void update_UpdatesAnime_WhenSuccessful() throws Exception {
        String request = readResourceFile("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("UPDATE /v1/animes throws ResponseStatusException when id is not found")
    void update_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception {
        String request = readResourceFile("anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    private String readResourceFile(String fileName) throws IOException {
        File file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();

        return new String(Files.readAllBytes(file.toPath()));
    }
}
