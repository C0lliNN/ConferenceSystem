package com.raphael.conferenceapp.management.mock;

import com.github.javafaker.Faker;
import com.raphael.conferenceapp.management.entity.Session;
import com.raphael.conferenceapp.management.persistence.SessionEntity;

import java.time.LocalDateTime;
import java.time.Month;

public class SessionMock {
    private static final Faker FAKER = Faker.instance();

    public static SessionEntity newSessionEntity() {
        return new SessionEntity(
                FAKER.random().nextLong(100),
                LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50),
                LocalDateTime.of(2021, Month.NOVEMBER, 20, 17, 50),
                FAKER.company().name(),
                FAKER.lorem().sentence(),
                FAKER.internet().url(),
                SpeakerMock.newSpeakerEntity()
        );
    }

    public static Session newSessionDomain() {
        return Session.builder()
                .id(FAKER.random().nextLong(100))
                .startTime(LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50))
                .endTime(LocalDateTime.of(2021, Month.NOVEMBER, 20, 17, 50))
                .title(FAKER.company().name())
                .description(FAKER.lorem().sentence())
                .accessLink(FAKER.internet().url())
                .speaker(SpeakerMock.newSpeakerDomain())
                .build();
    }
}