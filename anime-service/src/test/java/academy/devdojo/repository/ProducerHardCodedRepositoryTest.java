package academy.devdojo.repository;

import academy.devdojo.model.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProducerHardCodedRepositoryTest {
    //class alvo - InjectMocks

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
    }

    @Test
    @DisplayName("findAll returns a list with all produces")
    void findAll_ReturnSAllProducers_WhenSuccessful() {
        BDDMockito.when(producerData.getPRODUCERS()).thenReturn(PRODUCERS_LIST);

        List<Producer> producers = repository.findAll();
        Assertions.assertThat(producers)
                .isNotNull()
                .hasSize(PRODUCERS_LIST.size());
    }
}