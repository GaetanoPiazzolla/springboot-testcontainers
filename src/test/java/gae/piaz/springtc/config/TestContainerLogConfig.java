package gae.piaz.springtc.config;

import gae.piaz.springtc.TestApplication;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@Configuration
@Service
public class TestContainerLogConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestApplication.class);

    @Autowired
    private PostgreSQLContainer postgresSQLContainer;

    @Autowired
    private GenericContainer redisContainer;

    @Autowired
    private KafkaContainer kafkaContainer;

    @PostConstruct
    public void init() {

        // Attaching the log of the Containers we create to the log of the Spring-Boot app.
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOGGER);
        logConsumer.withPrefix("TC-LOG--->");

        postgresSQLContainer.followOutput(logConsumer);
        redisContainer.followOutput(logConsumer);
        kafkaContainer.followOutput(logConsumer);

    }

}
