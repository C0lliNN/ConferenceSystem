package com.raphael.conferenceapp.management.mock;

import com.github.javafaker.Faker;
import com.raphael.conferenceapp.management.entity.Session;
import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.persistence.SessionEntity;
import com.raphael.conferenceapp.management.persistence.SpeakerEntity;
import com.raphael.conferenceapp.management.usecase.request.CreateSessionRequest;

import java.time.LocalDateTime;
import java.time.Month;

public class SessionMock {
    private static final Faker FAKER = Faker.instance();

    public static SessionEntity newSessionEntity() {
        SpeakerEntity speaker = SpeakerMock.newSpeakerEntity();

        return new SessionEntity(
                FAKER.random().nextLong(100),
                LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50),
                LocalDateTime.of(2021, Month.NOVEMBER, 20, 17, 50),
                FAKER.company().name(),
                FAKER.lorem().sentence(),
                FAKER.internet().url(),
                FAKER.random().nextLong(100),
                speaker
        );
    }

    public static Session newSessionDomain() {
        Speaker speaker = SpeakerMock.newSpeakerDomain();

        return Session.builder()
                .id(FAKER.random().nextLong(100))
                .startTime(LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50))
                .endTime(LocalDateTime.of(2021, Month.NOVEMBER, 20, 17, 50))
                .title(FAKER.company().name())
                .description(FAKER.lorem().sentence())
                .accessLink(FAKER.internet().url())
                .conferenceId(FAKER.random().nextLong(100))
                .speaker(speaker)
                .build();
    }

    public static CreateSessionRequest newCreateRequest() {
        return new CreateSessionRequest(
                FAKER.company().name(),
                FAKER.lorem().sentence(),
                LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50),
                LocalDateTime.of(2021, Month.NOVEMBER, 20, 17, 50),
                FAKER.internet().url(),
                FAKER.random().nextLong(100),
                FAKER.random().nextLong(100)
        );
    }
}
