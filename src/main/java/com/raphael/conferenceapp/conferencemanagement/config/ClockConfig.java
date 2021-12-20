package com.raphael.conferenceapp.conferencemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfig {

    @Bean
    public Clock createClock() {
        return Clock.systemUTC();
    }
}