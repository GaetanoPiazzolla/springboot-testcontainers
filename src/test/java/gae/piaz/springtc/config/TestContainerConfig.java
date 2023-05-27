package gae.piaz.springtc.config;

import com.github.dockerjava.api.command.CreateContainerCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@Configuration
public class TestContainerConfig {

    public static final String POSTGRES_IMAGE = "postgres:15.1-alpine";
    public static final String REDIS_IMAGE = "redis:5.0.3-alpine";
    public static final String KAFKA_IMAGE = "confluentinc/cp-kafka:7.2.1";

    @Autowired
    private ResourceLoader resourceLoader;

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
                new PostgreSQLContainer(POSTGRES_IMAGE)
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
                new GenericContainer<>(REDIS_IMAGE)
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

        return new KafkaContainer(DockerImageName.parse(KAFKA_IMAGE))
                .withReuse(true).withCreateContainerCmdModifier(cmd -> {
                    cmd.withName("kafka");
                    cmd.getHostConfig()
                            .withMemory(memoryInBytes)
                            .withMemorySwap(memorySwapInBytes);
                });
    }

    @Bean
    @RestartScope
    // @ServiceConnection we don't need auto-connection in this case.
    public GenericContainer pythonContainer() throws IOException {

        final long memoryInBytes = 32L * 1024L * 1024L;
        final long memorySwapInBytes = 64L * 1024L * 1024L;

        Resource resource = resourceLoader.getResource("classpath:python/Dockerfile");
        return new GenericContainer<>(
                new ImageFromDockerfile().withDockerfile(resource.getFile().toPath()))
                // 5000 is the standard port of flask. check "ExternalPortConfig" for details
                .withExposedPorts(5000)
                .withReuse(true)
                .withCreateContainerCmdModifier(cmd -> {
                    cmd.withName("flaskapp");
                    cmd.getHostConfig()
                            .withMemory(memoryInBytes)
                            .withMemorySwap(memorySwapInBytes);
                });
    }

}