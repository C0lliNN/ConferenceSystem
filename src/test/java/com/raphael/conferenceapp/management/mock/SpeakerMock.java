package com.raphael.conferenceapp.management.mock;

import com.github.javafaker.Faker;
import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.persistence.SpeakerEntity;
import com.raphael.conferenceapp.management.usecase.request.CreateSpeakerRequest;

public class SpeakerMock {
    private static final Faker FAKER = Faker.instance();

    public static SpeakerEntity newSpeakerEntity() {
        return new SpeakerEntity(
                FAKER.random().nextLong(100),
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.internet().emailAddress(),
                FAKER.job().title()
        );
    }

    public static Speaker newSpeakerDomain() {
        return Speaker.builder()
                .id(FAKER.random().nextLong(100))
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(FAKER.internet().emailAddress())
                .professionalTitle(FAKER.job().title())
                .build();
    }

    public static CreateSpeakerRequest newCreateRequest() {
        return new CreateSpeakerRequest(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.internet().emailAddress(),
                FAKER.job().title()
        );
    }
}
