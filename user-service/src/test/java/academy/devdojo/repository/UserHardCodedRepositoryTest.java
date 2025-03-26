package academy.devdojo.repository;

import academy.devdojo.model.User;
import academy.devdojo.utils.UserUtils;
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
class UserHardCodedRepositoryTest {
    @InjectMocks
    private UserHardCodedRepository repository;
    @InjectMocks
    private UserUtils userUtils;
    @Mock
    private UserData userData;
    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
    }

    @Test
    @DisplayName("findAll returns all users when successful")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessful() {

        List<User> response = repository.findAll();

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(userList);
    }

    @Test
    @DisplayName("findAllByName returns objects found in list when successful")
    @Order(2)
    void findAllByName_ReturnsObjectsFoundInList_WhenSuccessful() {
        User expectedUser = userList.get(0);
        String nameToBeSearched = expectedUser.getFirstName();
        List<User> expectedResponse = List.of(expectedUser);

        List<User> response = repository.findAllByName(nameToBeSearched);

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("findAllByName returns an empty list when name is not found")
    @Order(3)
    void findAllByName_ReturnsEmptyList_WhenNameIsNotFound() {
        String randomName = "name not found";

        List<User> response = repository.findAllByName(randomName);

        Assertions.assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findById returns object found when successful")
    @Order(4)
    void findById_ReturnsObjectFound_WhenSuccessful() {
        User expectedUser = userList.get(0);
        Long idToBeSearched = expectedUser.getId();

        Optional<User> response = repository.findById(idToBeSearched);

        Assertions.assertThat(response)
                .isPresent()
                .containsSame(expectedUser);
    }

    @Test
    @DisplayName("findById returns empty optional when id is not found")
    @Order(5)
    void findById_ReturnsEmptyOptional_WhenIdIsNotFound() {
        Long randomId = 455268L;

        Optional<User> response = repository.findById(randomId);

        Assertions.assertThat(response)
                .isEmpty();
    }

    @Test
    @DisplayName("save saves object given when successful")
    void save_SavesObjectGiven_WhenSuccessful() {
        User userToBeSaved = userUtils.newUserToBeSaved();

        User response = repository.save(userToBeSaved);

        Assertions.assertThat(userList)
                .isNotEmpty()
                .isNotEmpty()
                .contains(response);
    }
}