package academy.devdojo.service;

import academy.devdojo.model.User;
import academy.devdojo.model.UserProfile;
import academy.devdojo.repository.UserProfileRepository;
import academy.devdojo.utils.ProfileUtils;
import academy.devdojo.utils.UserProfileUtils;
import academy.devdojo.utils.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileProfileServiceTest {
    @InjectMocks
    private UserProfileService service;
    @InjectMocks
    private UserProfileUtils userProfileUtils;
    @Mock
    private UserProfileRepository repository;
    @Spy
    private UserUtils userUtils;
    @Spy
    private ProfileUtils profileUtils;
    private List<UserProfile> userProfileList;

    @BeforeEach
    void init() {
        userProfileList = userProfileUtils.newUserProfileList();
    }

    @Test
    @DisplayName("findAll returns all userProfiles when successful")
    @Order(1)
    void findAll_ReturnsAllUserProfiles_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);
        List<UserProfile> response = service.findAll();

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(userProfileList);

        response.forEach(up -> Assertions.assertThat(up).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("findAllUsersByProfileId returns all found users when successful")
    @Order(2)
    void findAllUsersByProfileId_ReturnsAllFoundUsers_WhenSuccessful() {
        Long profileId = profileUtils.newProfileList().getFirst().getId();
        List<User> users = userProfileList.stream()
                .filter(up -> up.getId().equals(profileId))
                .map(UserProfile::getUser).toList();

        BDDMockito.when(repository.findAllUsersByProfileId(profileId)).thenReturn(users);

        List<User> response = service.findAllUsersByProfileId(profileId);

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(users);
    }
}