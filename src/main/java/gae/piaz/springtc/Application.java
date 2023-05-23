package gae.piaz.springtc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("gae.piaz.springtc")
@EntityScan("gae.piaz.springtc.data")
@EnableCaching
public class Application {

	// To run this locally (with 'testcontainers'): run Main in: test/gae.piaz.springtc.TestApplication
	// or use ./gradlew bootTestRun
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}



