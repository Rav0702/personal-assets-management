package allcount.poc;

import allcount.poc.core.configuration.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PocApplicationTests {

	@Test
	void contextLoads() {
	}

}
