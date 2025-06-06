package academy.devdojo.service;

import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserRepository;
import academy.devdojo.utils.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @InjectMocks
    private UserUtils userUtils;
    @Mock
    private UserRepository repository;
    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);
        List<User> response = service.findAll(null);

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(userList);
    }

    @Test
    @DisplayName("findAll returns user found in list when name is given")
    @Order(2)
    void findAll_ReturnsUserFoundInList_WhenNameIsGiven() {
        User expectedUser = userList.get(0);
        String nameToBeSearched = expectedUser.getFirstName();
        List<User> expectedResponse = List.of(expectedUser);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(nameToBeSearched)).thenReturn(expectedResponse);
        List<User> response = service.findAll(nameToBeSearched);

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("findAll returns an empty list when the name given not exists")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameGivenNotExists() {
        String randomName = "random-name";

        BDDMockito.when(repository.findByFirstNameIgnoreCase(randomName)).thenReturn(Collections.emptyList());
        List<User> response = service.findAll(randomName);

        Assertions.assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findByIdOrThrowsNotFoundException returns user found when successful")
    @Order(4)
    void findByIdOrThrowsNotFoundException_ReturnsUserFound_WhenSuccessful() {
        User userExpected = userList.get(0);
        Long idToBeSearched = userExpected.getId();

        BDDMockito.when(repository.findById(idToBeSearched)).thenReturn(Optional.of(userExpected));

        User response = service.findByIdOrThrowsNotFoundException(idToBeSearched);

        Assertions.assertThat(response)
                .isNotNull()
                .isEqualTo(userExpected);
    }

    @Test
    @DisplayName("findByIdOrThrowsNotFoundException throws ResponseStatusException when id is not found")
    @Order(5)
    void findByIdOrThrowsNotFoundException_ThrowsResponseStatusException_WhenIdIsNotFound() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        Long randomId = 1221L;

        Assertions.assertThatThrownBy(() -> service.findByIdOrThrowsNotFoundException(randomId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not Found");
    }

    @Test
    @DisplayName("save returns saved user when successful")
    @Order(6)
    void save_ReturnsSavedUser_WhenSuccessful() {
        User userToBeSaved = userUtils.newUserToBeSaved();

        BDDMockito.when(repository.save(userToBeSaved)).thenReturn(userToBeSaved);
        BDDMockito.when(repository.findByEmail(userToBeSaved.getEmail())).thenReturn(Optional.empty());

        User response = service.save(userToBeSaved);

        Assertions.assertThat(response)
                .isNotNull()
                .isEqualTo(userToBeSaved);
    }

    @Test
    @DisplayName("save throws EmailAlreadyExists when the email already exists")
    @Order(7)
    void save_ThrowsEmailAlreadyExists_WhenTheEmailAlreadyExists() {
        User savedUser = userList.getLast();
        User userToBeSaved = userUtils.newUserToBeSaved().withEmail(savedUser.getEmail());

        BDDMockito.when(repository.findByEmail(userToBeSaved.getEmail())).thenReturn(Optional.of(savedUser));

        Assertions.assertThatThrownBy(() -> service.save(userToBeSaved))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("update updates user when successful")
    @Order(8)
    void update_UpdatesUser_WhenSuccessful() {
        User userToBeUpdated = userList.get(0).withFirstName("New firstName");
        Long id = userToBeUpdated.getId();
        String email = userToBeUpdated.getEmail();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToBeUpdated));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(userToBeUpdated)).thenReturn(userToBeUpdated);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToBeUpdated));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when object to update is not found")
    @Order(9)
    void update_ThrowsResponseStatusException_WhenObjectToUpdateIsNotFound() {
        User userNotRegistered = userList.getFirst();

        BDDMockito.when(repository.findById(userNotRegistered.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.update(userNotRegistered))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not Found");
    }

    @Test
    @DisplayName("update throws EmailAlreadyExists when the email belong to another user")
    @Order(10)
    void update_ThrowsEmailAlreadyExists_WhenTheEmailBelongToAnotherUser() {
        User savedUser = userList.getLast();
        User userToUpdate = userList.getFirst().withEmail(savedUser.getEmail());

        Long id = userToUpdate.getId();
        String email = userToUpdate.getEmail();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("delete removes user when successful")
    @Order(11)
    void delete_RemovesUser_WhenSuccessful() {
        User userToBeDeleted = userList.get(0);
        Long idToBeDeleted = userToBeDeleted.getId();

        BDDMockito.when(repository.findById(idToBeDeleted)).thenReturn(Optional.of(userToBeDeleted));
        BDDMockito.doNothing().when(repository).delete(userToBeDeleted);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(idToBeDeleted));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when object to delete is not found")
    @Order(12)
    void delete_ThrowsResponseStatusException_WhenObjectToDeleteIsNotFound() {
        long randomId = 1255L;

        BDDMockito.when(repository.findById(randomId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.delete(randomId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not Found");
    }
}