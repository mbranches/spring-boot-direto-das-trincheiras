package academy.devdojo.service;

import academy.devdojo.model.User;
import academy.devdojo.repository.UserHardCodedRepository;
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
    private UserHardCodedRepository repository;
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

        BDDMockito.when(repository.findAllByName(nameToBeSearched)).thenReturn(expectedResponse);
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

        BDDMockito.when(repository.findAllByName(randomName)).thenReturn(Collections.emptyList());
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
    @DisplayName("findByIdOrThrowsNotFoundException throws not found exception when id is not found")
    void findByIdOrThrowsNotFoundException_ThrowsNotFoundException_WhenIdIsNotFound() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        Long randomId = 1221L;

        Assertions.assertThatThrownBy(() -> service.findByIdOrThrowsNotFoundException(randomId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not Found");
    }
}