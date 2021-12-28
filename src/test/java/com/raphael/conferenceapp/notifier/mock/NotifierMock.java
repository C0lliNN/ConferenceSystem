package com.raphael.conferenceapp.notifier.mock;

import com.github.javafaker.Faker;
import com.raphael.conferenceapp.notifier.entity.Conference;
import com.raphael.conferenceapp.notifier.entity.Participant;

import java.time.LocalDateTime;
import java.time.Month;

public class NotifierMock {
    private static final Faker FAKER = Faker.instance();

    public static Conference newConference() {
        return Conference.builder()
                .id(FAKER.random().nextLong(100))
                .title(FAKER.company().name())
                .description(FAKER.lorem().sentence())
                .startTime(LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50))
                .build();
    }

    public static Participant newParticipant() {
        return Participant.builder()
                .id(FAKER.random().nextLong(100))
                .name(FAKER.name().firstName())
                .email(FAKER.internet().emailAddress())
                .build();
    }
}
