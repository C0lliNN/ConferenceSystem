package com.raphael.conferenceapp.auth.mock;

import com.github.javafaker.Faker;
import com.raphael.conferenceapp.auth.entity.User;
import com.raphael.conferenceapp.auth.persistence.UserEntity;
import com.raphael.conferenceapp.auth.usecase.request.LoginRequest;
import com.raphael.conferenceapp.auth.usecase.request.RegisterRequest;

public class AuthMock {
    private static final Faker FAKER = Faker.instance();

    public static User newUserDomain() {
        return User.builder()
                .id(FAKER.random().nextLong(100))
                .name(FAKER.name().firstName())
                .email(FAKER.internet().emailAddress())
                .password(FAKER.lorem().sentence())
                .build();

    }

    public static UserEntity newUserEntity() {
        return new UserEntity(
                FAKER.random().nextLong(100),
                FAKER.name().firstName(),
                FAKER.internet().emailAddress(),
                FAKER.lorem().sentence()
        );
    }

    public static RegisterRequest newRegisterRequest() {
        return new RegisterRequest(
                FAKER.name().firstName(),
                FAKER.internet().emailAddress(),
                FAKER.internet().password()
        );
    }

    public static LoginRequest newLoginRequest() {
        return new LoginRequest(FAKER.internet().emailAddress(), FAKER.internet().password());
    }
}
