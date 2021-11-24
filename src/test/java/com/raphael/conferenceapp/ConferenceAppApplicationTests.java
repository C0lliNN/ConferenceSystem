package com.raphael.conferenceapp;

import com.raphael.conferenceapp.utils.initializer.DatabaseContainerInitializer;
import com.raphael.conferenceapp.utils.config.DatabaseTestAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@EnableAutoConfiguration
@SpringBootTest(
        classes = {
                DatabaseTestAutoConfiguration.class,
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ContextConfiguration(initializers = DatabaseContainerInitializer.class)
class ConferenceAppApplicationTests {

    @Test
    void contextLoads() {
    }

}
