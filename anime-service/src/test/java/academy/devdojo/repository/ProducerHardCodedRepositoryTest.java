package academy.devdojo.repository;

import academy.devdojo.model.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {
    //class do teste - InjectMocks
    //class dependecias - Mocks

    @InjectMocks
    private ProducerHardCodedRepository repository;
    @Mock
    private ProducerData producerData;

    private final List<Producer> PRODUCERS_LIST = new ArrayList<>();

    @BeforeEach
    void init() {
        Producer ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        Producer witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        Producer studioGiblin = Producer.builder().id(3L).name("Studio Giblin").createdAt(LocalDateTime.now()).build();
        PRODUCERS_LIST.addAll(List.of(ufotable, witStudio, studioGiblin));

        BDDMockito.when(producerData.getPRODUCERS()).thenReturn(PRODUCERS_LIST);
    }

    @Test
    @DisplayName("findAll returns a list with all produces")
    @Order(1)
    void findAll_ReturnSAllProducers_WhenSuccessful() {

        List<Producer> producers = repository.findAll();
        Assertions.assertThat(producers)
                .isNotNull()
                .hasSameElementsAs(PRODUCERS_LIST); //compara pelo equalsAndHashCode
    }

    @Test
    @DisplayName("findById returns a producer with given id")
    @Order(2)
    void findById_ReturnsProducerById_WhenSuccessful() {

        Producer expectedProducer = PRODUCERS_LIST.get(0);

        Optional<Producer> producers = repository.findById(1L);

        Assertions.assertThat(producers)
                .isPresent()
                .contains(expectedProducer);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {

        List<Producer> producers = repository.findByName(null);

        Assertions.assertThat(producers)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object with name exists")
    @Order(4)
    void findByName_ReturnsFoundProducerInList_WhenNameExists() {

        Producer producerExpected = PRODUCERS_LIST.get(0);
        String producerExpectedName = producerExpected.getName();

        List<Producer> producers = repository.findByName(producerExpectedName);

        Assertions.assertThat(producers)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(producers.get(0).getName()).isEqualTo(producerExpectedName);
    }

    @Test
    @DisplayName("save creates producer when successful")
    @Order(5)
    void save_CreatesProducer_WhenSuccessful() {
        Producer producerToSave = Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();
        Producer producerSaved = repository.save(producerToSave);

        Assertions.assertThat(producerSaved).isNotNull()
                .isEqualTo(producerToSave)
                .hasNoNullFieldsOrProperties();

        Optional<Producer> producerSavedInList = repository.findById(producerSaved.getId());
        Assertions.assertThat(producerSavedInList)
                .isPresent()
                .contains(producerToSave);
    }

    @Test
    @DisplayName("delete removes producer when successful")
    @Order(6)
    void delete_RemovesProducer_WhenSuccessful() {
        Producer producerToDelete = PRODUCERS_LIST.get(0);

        repository.delete(producerToDelete);

        List<Producer> producers = repository.findAll();

        Assertions.assertThat(producers).isNotEmpty().doesNotContain(producerToDelete);
    }

    @Test
    @DisplayName("update updates producer when successful")
    @Order(7)
    void update_UpdatesProducer_WhenSuccessful() {
        Producer producerToUpdate = PRODUCERS_LIST.get(0);
        producerToUpdate.setName("Aniplex");

        repository.update(producerToUpdate);

        Assertions.assertThat(this.PRODUCERS_LIST).contains(producerToUpdate);

        Optional<Producer> producerUpdated = repository.findById(producerToUpdate.getId());
        Assertions.assertThat(producerUpdated).isPresent();

        Assertions.assertThat(producerUpdated.get().getName()).isEqualTo(producerToUpdate.getName());
    }
}