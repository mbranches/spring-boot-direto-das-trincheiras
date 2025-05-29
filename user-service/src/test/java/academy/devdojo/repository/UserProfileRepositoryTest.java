package academy.devdojo.repository;

import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.model.User;
import academy.devdojo.model.UserProfile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileRepositoryTest extends IntegrationTestConfig {
    @Autowired
    private UserProfileRepository repository;

    @Test
    @DisplayName("findAll returns all userProfiles when successful")
    @Order(1)
    @Sql("/sql/user_profile/init_user_profile_2_users_1_profile.sql")
    void findAll_ReturnsAllUserProfiles_WhenSuccessful() {
        List<UserProfile> response = repository.findAll();

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    @DisplayName("findAll returns all users by profile id when successful")
    @Order(2)
    @Sql("/sql/user_profile/init_user_profile_2_users_1_profile.sql")
    void findAll_ReturnsAllUsersByProfileId_WhenSuccessful() {
        Long profileId = 1L;
        List<User> response = repository.findAllUsersByProfileId(profileId);

        Assertions.assertThat(response)
                .isNotNull()
                .hasSize(2)
                .doesNotContainNull()
                .isNotEmpty();
    }

}