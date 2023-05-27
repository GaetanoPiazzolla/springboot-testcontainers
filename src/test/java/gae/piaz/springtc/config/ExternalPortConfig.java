package gae.piaz.springtc.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;

//From the host's perspective Testcontainers actually exposes .exposePort() on a random free port.
// it-s possible to define a fixed port, but
// This is by design, to avoid port collisions that may arise with locally running software or in between parallel test runs.
@Configuration
public class ExternalPortConfig {

    @Autowired
    private GenericContainer pythonContainer;

    @PostConstruct
    public void initPort() {
        System.setProperty("EXTERNAL_CUSTOMER_SERVICE_PORT", String.valueOf(
                pythonContainer.getMappedPort(5000)
        ));
    }
}
