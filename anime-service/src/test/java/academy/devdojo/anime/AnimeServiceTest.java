package academy.devdojo.anime;

import academy.devdojo.model.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;
    @InjectMocks
    private AnimeUtils animeUtils;
    @Mock
    private AnimeRepository repository;
    private List<Anime> animeList;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns all animes when name is null")
    void findAll_ReturnsAllAnimes_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        List<Anime> animeList = service.findAll(null);

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .containsAnyElementsOf(this.animeList);
    }

    @Test
    @Order(2)
    @DisplayName("findAll returns list with found object when name found")
    void findAll_ReturnsListWithFoundObject_WhenNameFound() {
        Anime expectedAnime = this.animeList.getFirst();
        String expectedAnimeName = expectedAnime.getName();
        List<Anime> expectedList = Collections.singletonList(expectedAnime);
        BDDMockito.when(repository.findAllByNameContaining(expectedAnimeName)).thenReturn(expectedList);

        List<Anime> animeList = service.findAll(expectedAnimeName);

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedList);
    }

    @Test
    @Order(3)
    @DisplayName("findAll returns empty list when name not found")
    void findAll_ReturnsEmptyList_WhenNameNotFound() {
        BDDMockito.when(repository.findAllByNameContaining(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());

        List<Anime> animeList = service.findAll("a random name");

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("findAllPaginated returns all animes when when successful")
    void findAllPaginated_ReturnsAllAnimes_WhenSuccessful() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, animeList.size());
        PageImpl<Anime> pageAnime = new PageImpl<>(animeList, pageRequest, 1);

        BDDMockito.when(repository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(pageAnime);

        Page<Anime> response = service.findAllPageable(pageRequest);

        Assertions.assertThat(response)
                .hasSameElementsAs(animeList);
    }

    @Test
    @Order(4)
    @DisplayName("findByIdOrThrowNotFound returns found anime when successful")
    void findByIdOrThrowNotFound_ReturnsFoundAnime_WhenSuccessful() {
        Anime expectedAnime = this.animeList.getFirst();
        Long expectedAnimeId = expectedAnime.getId();
        BDDMockito.when(repository.findById(expectedAnimeId)).thenReturn(Optional.of(expectedAnime));

        Anime foundAnime = service.findByIdOrThrowNotFound(expectedAnimeId);

        Assertions.assertThat(foundAnime)
                .isNotNull()
                .isEqualTo(expectedAnime);
    }

    @Test
    @Order(5)
    @DisplayName("findByIdOrThrowNotFound throws ResponseStatusException when id is not found")
    void findByIdOrThrowNotFound_ThrowsResponseStatusException_WhenIdIsNotFound() {
        Long randomId = 13321910L;
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.findByIdOrThrowNotFound(randomId))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(6)
    @DisplayName("save returns created anime when successful")
    void save_ReturnsCreatedAnime_WhenSuccessful() {
        Anime animeToBeSaved = animeUtils.newAnimeToSave();
        BDDMockito.when(repository.save(animeToBeSaved)).thenReturn(animeToBeSaved);

        Anime savedAnime = service.save(animeToBeSaved);

        Assertions.assertThat(savedAnime)
                .isNotNull()
                .isEqualTo(animeToBeSaved)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Order(7)
    @DisplayName("delete remove anime when successful")
    void delete_RemoveAnime_WhenSuccessful() {
        Anime animeToBeDeleted = this.animeList.getFirst();
        Long animeToBeDeletedId = animeToBeDeleted.getId();

        BDDMockito.when(repository.findById(animeToBeDeletedId)).thenReturn(Optional.of(animeToBeDeleted));
        BDDMockito.doNothing().when(repository).delete(animeToBeDeleted);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToBeDeletedId));
    }

    @Test
    @Order(8)
    @DisplayName("delete throws ResponseStatusException when id is not found")
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() {
        Long randomId = 13321910L;
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.delete(randomId))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(9)
    @DisplayName("update updates anime when successful")
    void update_UpdatesAnime_WhenSuccessful() {
        Anime animeToBeUpdated = this.animeList.getFirst();
        animeToBeUpdated.setName("new name");
        Long animeToBeUpdatedId = animeToBeUpdated.getId();

        BDDMockito.when(repository.findById(animeToBeUpdatedId)).thenReturn(Optional.of(animeToBeUpdated));
        BDDMockito.when(repository.save(animeToBeUpdated)).thenReturn(animeToBeUpdated);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToBeUpdated));
    }

    @Test
    @Order(10)
    @DisplayName("update throws ResponseStatusException when id is not found")
    void update_ThrowsResponseStatusException_WhenIdIsNotFound() {
        Anime animeToBeUpdated = this.animeList.getFirst();
        animeToBeUpdated.setName("new name");
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.update(animeToBeUpdated))
                .isInstanceOf(ResponseStatusException.class);
    }
}
