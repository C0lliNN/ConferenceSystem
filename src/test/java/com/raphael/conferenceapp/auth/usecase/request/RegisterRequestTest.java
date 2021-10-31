package com.raphael.conferenceapp.auth.usecase.request;

import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.mock.AuthMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterRequestTest {

    @Nested
    @DisplayName("method: toUser()")
    class ToUserMethod {

        @Test
        @DisplayName("when called, then it should return an user object")
        void whenCalled_shouldReturnAnUserObject() {
            RegisterRequest request = AuthMock.newRegisterRequest();

            User expected = new User(null, request.getName(), request.getEmail(), request.getPassword());
            User actual = request.toUser();

            assertThat(actual).isEqualTo(expected);
        }
    }
}