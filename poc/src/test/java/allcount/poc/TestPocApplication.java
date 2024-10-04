package allcount.poc;

import allcount.poc.core.configuration.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestPocApplication {

	public static void main(String[] args) {
		SpringApplication.from(PocApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
