package academy.devdojo.repository;

import academy.devdojo.model.Anime;
import academy.devdojo.utils.AnimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {
    private static final Logger log = LogManager.getLogger(AnimeHardCodedRepositoryTest.class);
    @InjectMocks
    private AnimeHardCodedRepository repository;
    @InjectMocks
    private AnimeUtils animeUtils;
    @Mock
    private AnimeData animeData;

    private List<Anime> animeList;

    @BeforeEach
    void init() {

        animeList = animeUtils.newAnimeList();

        BDDMockito.when(animeData.getANIMES()).thenReturn(animeList);
    }

    @Test
    @DisplayName("findAll returns a list with all animes when successful")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenSuccessful() {
        List<Anime> animesResponse = repository.findAll();

        Assertions.assertThat(animesResponse)
                .isNotNull()
                .isNotEmpty()
                .hasSameElementsAs(this.animeList);
    }

    @Test
    @DisplayName("findById return an anime with given id when successful")
    @Order(2)
    void findById_ReturnsAnimeById_WhenSuccessful() {
        Anime animeExpected = this.animeList.get(0);

        Optional<Anime> animeOptional = repository.findById(animeExpected.getId());

        Assertions.assertThat(animeOptional)
                .isPresent()
                .contains(animeExpected);
    }

    @Test
    @DisplayName("findByName return a list of anime with object found when name exists")
    @Order(3)
    void findByName_ReturnsFoundAnimeInList_WhenNameExists() {
        Anime animeExpected = this.animeList.get(0);
        String animeExpectedName = animeExpected.getName();

        List<Anime> animes = repository.findByName(animeExpectedName);

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(animeExpectedName);
    }

    @Test
    @DisplayName("findByName return an empty list when name not exists")
    @Order(4)
    void findByName_ReturnsEmptyList_WhenNameNotExists() {
        List<Anime> animes = repository.findByName("XAXAXAxuxuxuXIXIIXI");

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save creates an anime and return the object created whe successful")
    void save_CreatesAnimeAndReturnsObjectCreated_WhenSuccessful() {
        Anime animeToBeSaved = animeUtils.newAnimeToSave();

        Anime animeSaved = repository.save(animeToBeSaved);

        Assertions.assertThat(animeSaved).isEqualTo(animeToBeSaved).hasNoNullFieldsOrProperties();

        Assertions.assertThat(this.animeList).contains(animeSaved);
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {
        Anime animeToBeDeleted = this.animeList.get(0);

        repository.delete(animeToBeDeleted);

        Assertions.assertThat(this.animeList)
                .isNotEmpty()
                .doesNotContain(animeToBeDeleted);
    }

    @Test
    @DisplayName("update updates anime when successful")
    void update_UpdatesAnime_WhenSuccessful() {
        Anime animeToBeUpdated = this.animeList.get(0);
        animeToBeUpdated.setName("altered anime");

        repository.update(animeToBeUpdated);

        Optional<Anime> animeOptional = repository.findById(animeToBeUpdated.getId());
        Assertions.assertThat(animeOptional)
                .isPresent()
                .contains(animeToBeUpdated);

        Assertions.assertThat(animeOptional.get().getName())
                .isEqualTo(animeToBeUpdated.getName());
    }


}
