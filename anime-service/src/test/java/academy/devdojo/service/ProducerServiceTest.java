package academy.devdojo.service;

import academy.devdojo.model.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import academy.devdojo.utils.ProducerUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService service;
    @Mock
    private ProducerHardCodedRepository repository;
    @InjectMocks
    private ProducerUtils producerUtils;
    private List<Producer> producerList;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("findAll returns a list with all produces when name is null")
    @Order(1)
    void findAll_ReturnSAllProducers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        List<Producer> producers = service.findAll(null);
        Assertions.assertThat(producers)
                .isNotNull()
                .hasSameElementsAs(producerList); //compara pelo equalsAndHashCode
    }

    @Test
    @DisplayName("findAll returns list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundProducerInList_WhenNameExists() {
        Producer expectedProducer = producerList.get(0);
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

    @Test
    @DisplayName("findByIdOrThrowNotFound returns a producer with given id when successful")
    @Order(4)
    void findByIdOrThrowNotFound_ReturnsProducerById_WhenSuccessful() {
        Producer expectedProducer = producerList.get(0);
        Long idToBeFound = expectedProducer.getId();
        BDDMockito.when(repository.findById(idToBeFound)).thenReturn(Optional.of(expectedProducer));

        Producer producerFound = service.findByIdOrThrowNotFound(idToBeFound);

        Assertions.assertThat(producerFound)
                .isNotNull()
                .isEqualTo(expectedProducer);
    }

    @Test
    @DisplayName("findByIdOrThrowNotFound throws ResponseStatusException when producer is not found")
    @Order(5)
    void findByIdOrThrowNotFound_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        Long randomId = 8993813123813129L;
        BDDMockito.when(repository.findById(randomId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.findByIdOrThrowNotFound(randomId))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save returns the created producer when successful")
    @Order(6)
    void save_ReturnsCreatedProducer_WhenSuccessful() {
        Producer producerToBeSaved = producerUtils.newProducerToSave();
        BDDMockito.when(repository.save(producerToBeSaved)).thenReturn(producerToBeSaved);

        Producer savedProducer = service.save(producerToBeSaved);

        Assertions.assertThat(savedProducer)
                .isNotNull()
                .isEqualTo(producerToBeSaved)
                .hasNoNullFieldsOrProperties();
   }

    @Test
    @DisplayName("delete removes producer when successful")
    @Order(7)
    void delete_RemovesProducer_WhenSuccessful() {
        Producer producerToBeDeleted = producerList.get(0);
        Long idToBeDeleted = producerToBeDeleted.getId();
        BDDMockito.when(repository.findById(idToBeDeleted)).thenReturn(Optional.of(producerToBeDeleted));

        BDDMockito.doNothing().when(repository).delete(producerToBeDeleted);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(idToBeDeleted));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when producer is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        Long randomId = 8993813123813129L;
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.delete(randomId))
                .isInstanceOf(ResponseStatusException.class);
    }


    @Test
    @DisplayName("update updates producer when successful")
    @Order(9)
    void update_UpdatesProducer_WhenSuccessful() {
        Producer producerToBeUpdated = producerList.get(0);
        producerToBeUpdated.setName("New name for producer");
        Long idToBeUpdated = producerToBeUpdated.getId();

        BDDMockito.when(repository.findById(idToBeUpdated)).thenReturn(Optional.of(producerToBeUpdated));
        BDDMockito.doNothing().when(repository).update(producerToBeUpdated);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToBeUpdated));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when producer is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        Producer producerToBeUpdated = producerList.get(0);

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.update(producerToBeUpdated))
                .isInstanceOf(ResponseStatusException.class);
    }

}