package academy.devdojo.controller;

import academy.devdojo.model.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
import academy.devdojo.utils.FileUtils;
import academy.devdojo.utils.ProducerUtils;
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

import java.util.List;

//Slices test -> é parecido com teste de integração, porém starta só os beans mais importantes
@WebMvcTest(controllers = ProducerController.class) //se não definir, por padrão vai testar para todos os controllers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan("academy.devdojo")
class ProducerControllerTest {
    private static final String URL = "/v1/producers";
    @MockBean //No contexto do spring utilizar esse
    private ProducerData producerData;
    @SpyBean
    private ProducerHardCodedRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProducerUtils producerUtils;
    private List<Producer> producerList;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
        BDDMockito.when(producerData.getPRODUCERS()).thenReturn(producerList);
    }

    @Test
    @DisplayName("GET /v1/producers returns a list with all produces when name is null")
    @Order(1)
    void findAll_ReturnSAllProducers_WhenArgumentIsNull() throws Exception {
        String responseExpected = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected));
    }

    @Test
    @DisplayName("GET /v1/producers?name=Ufotable returns list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundProducerInList_WhenNameExists() throws Exception {
        String responseExpected = fileUtils.readResourceFile("producer/get-producer-ufotable-name-200.json");
        String name = "Ufotable";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected));
    }

    @Test
    @DisplayName("GET /v1/producers?name=NomeQueNaoExiste returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameNotFound() throws Exception{
        String responseExpected = fileUtils.readResourceFile("producer/get-producer-nomequenaoexiste-name-200.json");
        String name = "NomeQueNaoExiste";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected));
    }

    @Test
    @DisplayName("GET /v1/producers/1 returns producer found when successful")
    @Order(4)
    void findById_ReturnsProducerFound_WhenSuccessful() throws Exception{
        String responseExpected = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected));
    }

    @Test
    @DisplayName("GET /v1/producers/99 throws NotFoundException 404 when producer not found")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenProducerNotFound() throws Exception{
        Long randomId = 99L;
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", randomId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
        //Body vem vazio
    }

    @Test
    @DisplayName("POST /v1/producers returns the created producer when successful")
    @Order(6)
    void save_ReturnsCreatedProducer_WhenSuccessful() throws Exception {
        Producer producerSaved = producerUtils.newProducerToSave();
        BDDMockito.when(repository.save(ArgumentMatchers.any(Producer.class))).thenReturn(producerSaved);

        String request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
        String responseExpected = fileUtils.readResourceFile("producer/post-response-producer-201.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .header("x-api-key", "v1")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(responseExpected));
    }

    @Test
    @DisplayName("PUT /v1/producers updates producer when successful")
    @Order(7)
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        String request = fileUtils.readResourceFile("producer/put-request-producer-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent() );
    }

    @Test
    @DisplayName("PUT /v1/producers throws NotFoundException when producer is not found")
    @Order(8)
    void update_ThrowsNotFoundException_WhenProducerIsNotFound() throws Exception {
        String request = fileUtils.readResourceFile("producer/put-request-producer-404.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }

    @Test
    @DisplayName("DELETE /v1/producers/1 removes producer when successful")
    @Order(9)
    void delete_RemovesProducer_WhenSuccessful() throws Exception {
        Producer producerToBeDeleted = this.producerList.get(0);
        Long idToBeDeleted = producerToBeDeleted.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", idToBeDeleted))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /v1/producers/99 throws NotFoundException when producer is not found")
    @Order(10)
    void delete_ThrowsNotFoundException_WhenProducerIsNotFound() throws Exception {
        Long randomId = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", randomId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }

}