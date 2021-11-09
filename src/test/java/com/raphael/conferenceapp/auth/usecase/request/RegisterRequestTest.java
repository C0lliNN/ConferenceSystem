package com.raphael.conferenceapp.auth.usecase.request;

import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.auth.mock.AuthMock;
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

            User expected = User.builder()
                    .name(request.name())
                    .email(request.email())
                    .password(request.password())
                    .build();
            User actual = request.toUser();

            assertThat(actual).isEqualTo(expected);
        }
    }
}