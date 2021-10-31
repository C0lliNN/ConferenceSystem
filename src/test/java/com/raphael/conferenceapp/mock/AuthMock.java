package com.raphael.conferenceapp.mock;

import com.github.javafaker.Faker;
import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.auth.usecase.request.LoginRequest;
import com.raphael.conferenceapp.auth.usecase.request.RegisterRequest;

public class AuthMock {
    private static final Faker FAKER = Faker.instance();

    public static User newUserDomain() {
        return new User(
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
