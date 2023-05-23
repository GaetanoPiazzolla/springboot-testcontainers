package gae.piaz.springtc.config;

import com.github.dockerjava.api.command.CreateContainerCmd;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestContainerConfig {

    @Bean
    // This annotation is used to indicate that this bean will not be re-created if the application
    // restarts due to spring-dev-tools.
    @RestartScope
    // By default, ServiceConnection will create all applicable connection details beans
    // for a given Container.
    // For example, a PostgreSQLContainer will create both JdbcConnectionDetails and R2dbcConnectionDetails.
    @ServiceConnection(type = JdbcConnectionDetails.class)
    public PostgreSQLContainer postgreSQLContainer() {

        final long memoryInBytes = 64L * 1024L * 1024L;
        final long memorySwapInBytes = 128L * 1024L * 1024L;

        PostgreSQLContainer container = (PostgreSQLContainer)
                new PostgreSQLContainer("postgres:15.1-alpine")
                        .waitingFor(Wait.forLogMessage(".*PostgreSQL init process complete;.*\\n", 1))
                        // The Reusable Containers feature keeps the same containers running between test sessions
                        .withReuse(true)
                        .withCreateContainerCmdModifier(cmd -> {
                            ((CreateContainerCmd) cmd).withName("postgres");
                            ((CreateContainerCmd) cmd).getHostConfig()
                                    .withMemory(memoryInBytes)
                                    .withMemorySwap(memorySwapInBytes);
                        });

        return container;
    }

    @Bean
    @RestartScope
    // Autoconfig for ServiceConnection for redis images works only if Container is named "redis"
    @ServiceConnection(name = "redis")
    public GenericContainer redisContainer() {

        final long memoryInBytes = 32L * 1024L * 1024L;
        final long memorySwapInBytes = 64L * 1024L * 1024L;

        GenericContainer<?> redis =
                new GenericContainer<>("redis:5.0.3-alpine")
                        .withExposedPorts(6379)
                        .withReuse(true)
                        .withCreateContainerCmdModifier(cmd -> {
                            cmd.withName("redis");
                            cmd.getHostConfig()
                                    .withMemory(memoryInBytes)
                                    .withMemorySwap(memorySwapInBytes);
                        });
        return redis;
    }

    @Bean
    @RestartScope
    @ServiceConnection
    public KafkaContainer kafkaContainer() {
        final long memoryInBytes = 512L * 1024L * 1024L;
        final long memorySwapInBytes = 1024L * 1024L * 1024L;

        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"))
                .withReuse(true).withCreateContainerCmdModifier(cmd -> {
                    cmd.withName("kafka");
                    cmd.getHostConfig()
                            .withMemory(memoryInBytes)
                            .withMemorySwap(memorySwapInBytes);
                });
    }


}
