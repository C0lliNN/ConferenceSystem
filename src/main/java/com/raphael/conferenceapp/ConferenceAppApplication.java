package com.raphael.conferenceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConferenceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferenceAppApplication.class, args);
    }

}
