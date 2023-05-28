package gae.piaz.springtc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
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

    public static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:15.1-alpine");
    public static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:5.0.3-alpine");
    public static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:7.2.1");

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    // This annotation is used to indicate that this bean will not be re-created if the application
    // restarts due to spring-dev-tools.
    // @org.springframework.boot.devtools.restart.RestartScope
    // By default, ServiceConnection will create all applicable connection details beans
    // for a given Container.
    // For example, a PostgreSQLContainer will create both JdbcConnectionDetails and R2dbcConnectionDetails.
    @ServiceConnection(type = JdbcConnectionDetails.class)
    public PostgresContainer postgresContainer() {

        final long memoryInBytes = 64L * 1024L * 1024L;
        final long memorySwapInBytes = 128L * 1024L * 1024L;

        return new PostgresContainer(POSTGRES_IMAGE)
                .waitingFor(Wait.forLogMessage(".*PostgreSQL init process complete;.*\\n", 1))
                // The Reusable Containers feature keeps the same containers running between test sessions
                .withReuse(true)
                .withCreateContainerCmdModifier(cmd -> {
                    cmd.withName("postgres");
                    cmd.getHostConfig()
                            .withMemory(memoryInBytes)
                            .withMemorySwap(memorySwapInBytes);
                });
    }

    @Bean
    // Autoconfig for ServiceConnection for redis images works only if Container is named "redis"
    @ServiceConnection(name = "redis")
    public RedisContainer redisContainer() {

        final long memoryInBytes = 32L * 1024L * 1024L;
        final long memorySwapInBytes = 64L * 1024L * 1024L;

        return new RedisContainer(REDIS_IMAGE)
                .withExposedPorts(6379)
                .withReuse(true)
                .withCreateContainerCmdModifier(cmd -> {
                    cmd.withName("redis");
                    cmd.getHostConfig()
                            .withMemory(memoryInBytes)
                            .withMemorySwap(memorySwapInBytes);
                });
    }

    @Bean
    @ServiceConnection
    public KafkaContainer kafkaContainer() {
        final long memoryInBytes = 512L * 1024L * 1024L;
        final long memorySwapInBytes = 1024L * 1024L * 1024L;

        return new KafkaContainer(KAFKA_IMAGE)
                .withReuse(true).withCreateContainerCmdModifier(cmd -> {
                    cmd.withName("kafka");
                    cmd.getHostConfig()
                            .withMemory(memoryInBytes)
                            .withMemorySwap(memorySwapInBytes);
                });
    }

    @Bean
    // @ServiceConnection we don't need auto-connection in this case.
    public PythonContainer pythonContainer() throws IOException {

        final long memoryInBytes = 32L * 1024L * 1024L;
        final long memorySwapInBytes = 64L * 1024L * 1024L;

        Resource resource = resourceLoader.getResource("classpath:python/Dockerfile");
        return new PythonContainer(
                new ImageFromDockerfile().withDockerfile(resource.getFile().toPath()))
                // 5000 is the standard port of flask. check "ExternalPortConfig" for details
                .withExposedPorts(5000)
                .withCreateContainerCmdModifier(cmd -> {
                    cmd.withName("flaskapp");
                    cmd.getHostConfig()
                            .withMemory(memoryInBytes)
                            .withMemorySwap(memorySwapInBytes);
                });
    }

}

// Custom classes to avoid "Raw use of parameterized class 'GenericContainer'"
// Test-Container use self-typing mechanism to make fluent method works even with extended classes.
// https://stackoverflow.com/a/57077189
class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {
    public PostgresContainer(DockerImageName image) {
        super(image);
    }
}

class RedisContainer extends GenericContainer<RedisContainer> {
    public RedisContainer(DockerImageName image) {
        super(image);
    }
}

class PythonContainer extends GenericContainer<PythonContainer> {
    public PythonContainer(ImageFromDockerfile image) {
        super(image);
    }
}
