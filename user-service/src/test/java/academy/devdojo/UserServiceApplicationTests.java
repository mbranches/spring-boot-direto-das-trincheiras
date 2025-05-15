package academy.devdojo;

import academy.devdojo.config.IntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("itest")
class UserServiceApplicationTests extends IntegrationTestConfig {

	@Test
	void contextLoads() {
	}

}
