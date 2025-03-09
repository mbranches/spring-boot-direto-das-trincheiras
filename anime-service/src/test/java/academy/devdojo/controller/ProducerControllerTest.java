package academy.devdojo.controller;

import academy.devdojo.mapper.ProducerMapperImpl;
import academy.devdojo.model.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
import academy.devdojo.service.ProducerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Slices test -> é parecido com teste de integração, porém starta só os beans mais importantes
@WebMvcTest(controllers = ProducerController.class) //se não definir, por pa    drão vai testar para todos os controllers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class}) //não importa por padrões: service, component e repository
class ProducerControllerTest {
    @MockBean //No contexto do spring utilizar esse
    private ProducerData producerData;
    @Autowired
    private MockMvc mockMvc;private final List<Producer> PRODUCERS_LIST = new ArrayList<>();

    @BeforeEach
    void init() {
        Producer ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        Producer witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        Producer studioGiblin = Producer.builder().id(3L).name("Studio Giblin").createdAt(LocalDateTime.now()).build();
        PRODUCERS_LIST.addAll(List.of(ufotable, witStudio, studioGiblin));

        BDDMockito.when(producerData.getPRODUCERS()).thenReturn(PRODUCERS_LIST);
    }

    @Test
    @DisplayName("findAll returns a list with all produces when name is null")
    @Order(1)
    void findAll_ReturnSAllProducers_WhenArgumentIsNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L));
    }
}