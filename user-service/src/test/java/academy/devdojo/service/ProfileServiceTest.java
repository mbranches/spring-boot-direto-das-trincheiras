package academy.devdojo.service;

import academy.devdojo.model.Profile;
import academy.devdojo.repository.ProfileRepository;
import academy.devdojo.utils.ProfileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileService service;
    @InjectMocks
    private ProfileUtils profileUtils;
    @Mock
    private ProfileRepository repository;
    private List<Profile> profileList;

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("findAll returns all profiles when successful")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);
        List<Profile> response = service.findAll();

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(profileList);
    }

    @Test
    @DisplayName("save returns saved profile when successful")
    @Order(2)
    void save_ReturnsSavedProfile_WhenSuccessful() {
        Profile profileToBeSaved = profileUtils.newProfileToBeSaved();
        Profile profileSaved = profileUtils.newProfileSaved();

        BDDMockito.when(repository.save(profileToBeSaved)).thenReturn(profileSaved);

        Profile response = service.save(profileToBeSaved);

        Assertions.assertThat(response)
                .isNotNull()
                .isEqualTo(profileSaved);
    }
}