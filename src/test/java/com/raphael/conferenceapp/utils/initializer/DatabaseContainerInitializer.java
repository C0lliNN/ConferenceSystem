package com.raphael.conferenceapp.utils.initializer;

import org.junit.ClassRule;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

@ContextConfiguration
public class DatabaseContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String POSTGRES_IMAGE = "postgres:13.2";

    @ClassRule
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(POSTGRES_IMAGE);

    static {
        POSTGRES_CONTAINER.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        final PropertySource<Map<String, Object>> properties = new MapPropertySource(
                "DatabaseContainerInitializer",
                Map.of(
                        "spring.flyway.baseline-on-migrate", true,
                        "spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl(),
                        "spring.datasource.username", POSTGRES_CONTAINER.getUsername(),
                        "spring.datasource.password", POSTGRES_CONTAINER.getPassword()
                )
        );

        applicationContext
                .getEnvironment()
                .getPropertySources()
                .addFirst(properties);
    }
}

