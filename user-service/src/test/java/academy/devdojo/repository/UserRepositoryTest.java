package academy.devdojo.repository;

import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.model.User;
import academy.devdojo.utils.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserUtils.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest extends IntegrationTestConfig {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserUtils userUtils;

    @Test
    @DisplayName("findAll returns all users when successful")
    @Order(1)
    @Sql("/sql/user/init_one_user.sql")
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        List<User> response = repository.findAll();

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    @DisplayName("save returns saved user when successful")
    @Order(2)
    void save_ReturnsSavedUser_WhenSuccessful() {
        User userToBeSaved = userUtils.newUserToBeSaved().withId(null);

        User response = repository.save(userToBeSaved);

        Assertions.assertThat(response)
                .isNotNull();

        Assertions.assertThat(response.getId())
                .isNotNull();
    }
}