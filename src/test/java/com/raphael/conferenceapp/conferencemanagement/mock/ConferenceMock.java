package com.raphael.conferenceapp.conferencemanagement.mock;

import com.github.javafaker.Faker;
import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.persistence.ConferenceEntity;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateConferenceRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchConferencesRequest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

public class ConferenceMock {
    private static final Faker FAKER = Faker.instance();

    public static ConferenceEntity newConferenceEntity() {
        return new ConferenceEntity(
                FAKER.random().nextLong(100),
                FAKER.company().name(),
                FAKER.lorem().sentence(),
                LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50),
                LocalDateTime.of(2021, Month.NOVEMBER, 27, 15, 50),
                FAKER.random().nextInt(0, 100),
                FAKER.random().nextInt(0, 100),
                FAKER.random().nextLong(100),
                Collections.emptyList()
        );
    }

    public static Conference newConferenceDomain() {
        return Conference.builder()
                .id(FAKER.random().nextLong(100))
                .title(FAKER.company().name())
                .description(FAKER.lorem().sentence())
                .startTime(LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50))
                .endTime(LocalDateTime.of(2021, Month.NOVEMBER, 27, 15, 50))
                .totalParticipants(FAKER.random().nextInt(0, 100))
                .participantLimit(FAKER.random().nextInt(0, 100))
                .userId(FAKER.random().nextLong(100))
                .sessions(Collections.emptyList())
                .build();
    }

    public static CreateConferenceRequest newCreateRequest() {
        return new CreateConferenceRequest(
                FAKER.company().name(),
                FAKER.lorem().sentence(),
                LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50),
                LocalDateTime.of(2021, Month.NOVEMBER, 27, 15, 50),
                FAKER.random().nextInt(0, 100)
        );
    }

    public static SearchConferencesRequest newSearchRequest() {
        return new SearchConferencesRequest(
                FAKER.company().name(),
                LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50),
                LocalDateTime.of(2021, Month.NOVEMBER, 27, 15, 50),
                FAKER.random().nextLong(100) + 1,
                FAKER.random().nextLong(100) + 1,
                FAKER.random().nextLong(100) + 1
        );
    }
}
