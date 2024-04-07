package edu.java.scrapper;

import java.io.File;
import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.SneakyThrows;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@DirtiesContext
public abstract class IntegrationEnvironment {
    public static PostgreSQLContainer<?> POSTGRES;
    public static KafkaContainer KAFKA;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);

        KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));
        KAFKA.start();
    }

    @SneakyThrows private static void runMigrations(JdbcDatabaseContainer<?> container) {
        Database database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(container.createConnection("")));
        Liquibase liquibase =
            new Liquibase("master.xml",
                new DirectoryResourceAccessor(new File(".").toPath().toAbsolutePath().getParent().getParent()
                    .resolve("migrations")),
                database
            );
        updateCommand(liquibase).execute();
    }

    @SneakyThrows
    private static CommandScope updateCommand(Liquibase liquibase) {
        CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);

        return updateCommand.addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, liquibase.getDatabase())
            .addArgumentValue(UpdateCommandStep.CHANGELOG_ARG, liquibase.getDatabaseChangeLog());
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("app.use-queue", () -> true);
    }
}
