package allcount.poc;

import org.springframework.boot.SpringApplication;

public class TestPocApplication {

	public static void main(String[] args) {
		SpringApplication.from(PocApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
