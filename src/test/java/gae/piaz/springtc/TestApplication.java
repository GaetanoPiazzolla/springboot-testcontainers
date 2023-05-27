package gae.piaz.springtc;

import org.springframework.boot.SpringApplication;

public class TestApplication {

    // It's possible to run this from any IDE or through the gradle command "bootTestRun".
    // The name of the test application should be Test<NameOfApplication> and needs to be placed
    // in the same directory structure
    public static void main(String[] args) {
        SpringApplication
                .from(Application::main)
                .run(args);
    }

}
