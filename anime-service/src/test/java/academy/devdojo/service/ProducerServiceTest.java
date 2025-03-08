package academy.devdojo.service;

import academy.devdojo.model.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService service;
    @Mock
    private ProducerHardCodedRepository repository;
    private final List<Producer> PRODUCERS_LIST = new ArrayList<>();

    @BeforeEach
    void init() {
        Producer ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        Producer witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        Producer studioGiblin = Producer.builder().id(3L).name("Studio Giblin").createdAt(LocalDateTime.now()).build();
        PRODUCERS_LIST.addAll(List.of(ufotable, witStudio, studioGiblin));
    }

    @Test
    @DisplayName("findAll returns a list with all produces when name is null")
    @Order(1)
    void findAll_ReturnSAllProducers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(PRODUCERS_LIST);

        List<Producer> producers = service.findAll(null);
        Assertions.assertThat(producers)
                .isNotNull()
                .hasSameElementsAs(PRODUCERS_LIST); //compara pelo equalsAndHashCode
    }

    @Test
    @DisplayName("findAll returns list with found object with name exists")
    @Order(2)
    void findAll_ReturnsFoundProducerInList_WhenNameExists() {
        Producer expectedProducer = PRODUCERS_LIST.get(0);
        String expectedProducerName = expectedProducer.getName();
        List<Producer> expectedList = Collections.singletonList(expectedProducer);

        BDDMockito.when(repository.findByName(expectedProducerName)).thenReturn(expectedList);

        List<Producer> producers = service.findAll(expectedProducerName);

        Assertions.assertThat(producers)
                .isNotNull()
                .isNotEmpty()
                .containsAll(expectedList);
    }

    @Test
    @DisplayName("findAll returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameNotFound() {
        String randomName = "name not found-----";
        BDDMockito.when(repository.findByName(randomName)).thenReturn(Collections.emptyList());

        List<Producer> producers = service.findAll(randomName);

        Assertions.assertThat(producers)
                .isNotNull()
                .isEmpty();
    }

}