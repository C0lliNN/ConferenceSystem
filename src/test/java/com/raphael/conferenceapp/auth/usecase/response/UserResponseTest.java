package com.raphael.conferenceapp.auth.usecase.response;

import com.raphael.conferenceapp.auth.entity.User;
import com.raphael.conferenceapp.auth.mock.AuthMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Nested
    @DisplayName("method: fromUser(User)")
    class FromUserMethod {

        @Test
        @DisplayName("when called, then it should create the response")
        void whenCalled_shouldCreateTheResponse() {
            User user = AuthMock.newUserDomain();

            UserResponse expected = new UserResponse(user.getId(), user.getName(), user.getEmail(), null);
            UserResponse actual = UserResponse.fromUser(user);

            assertThat(actual).isEqualTo(expected);
        }
    }
}