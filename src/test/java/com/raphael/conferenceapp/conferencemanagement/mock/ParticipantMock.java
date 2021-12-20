package com.raphael.conferenceapp.conferencemanagement.mock;

import com.github.javafaker.Faker;
import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.persistence.ParticipantEntity;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchParticipantsRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SubscribeRequest;

import java.util.concurrent.TimeUnit;

public class ParticipantMock {
    private static final Faker FAKER = Faker.instance();

    public static ParticipantEntity newParticipantEntity() {
        return new ParticipantEntity(
                FAKER.random().nextLong(1000L),
                FAKER.name().name(),
                FAKER.internet().emailAddress(),
                FAKER.date().future(20, TimeUnit.DAYS).toInstant(),
                FAKER.random().nextLong(100L)
        );
    }

    public static Participant newParticipantDomain() {
        return Participant.builder()
                .id(FAKER.random().nextLong(1000L))
                .name(FAKER.name().name())
                .email(FAKER.internet().emailAddress())
                .subscribedAt(FAKER.date().future(20, TimeUnit.DAYS).toInstant())
                .conferenceId(FAKER.random().nextLong(100L))
                .build();
    }

    public static SearchParticipantsRequest newSearchRequest() {
        return new SearchParticipantsRequest(
                FAKER.name().name(),
                FAKER.internet().emailAddress(),
                FAKER.random().nextLong(100L),
                12L,
                4L
        );
    }

    public static SubscribeRequest newSubscribeRequest() {
        return new SubscribeRequest(
                FAKER.name().name(),
                FAKER.internet().emailAddress(),
                FAKER.random().nextLong(100L)
        );
    }
}
