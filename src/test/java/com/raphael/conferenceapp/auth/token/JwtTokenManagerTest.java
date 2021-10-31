package com.raphael.conferenceapp.auth.token;

import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.mock.AuthMock;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class JwtTokenManagerTest {
    private JwtTokenManager tokenManager;
    private final User user = AuthMock.newUserDomain();

    private static final String SECRET = "some-secret";

    @BeforeEach
    void setUp() {
        this.tokenManager = new JwtTokenManager(SECRET, 60000L);
    }

    @Nested
    @DisplayName("method: generateTokenForUser(User)")
    class GenerateTokenForUserMethod {

        @Test
        @DisplayName("when called, then it should generate a token for the user")
        void whenCalled_shouldGenerateATokenForTheUser() {
            String token = tokenManager.generateTokenForUser(user);

            assertThatCode(() -> Jwts.parser().setSigningKey(SECRET.getBytes()).parse(token))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("method: extractUserFromToken(String)")
    class ExtractUserFromTokenMethod {

        @Test
        @DisplayName("when called, then it should extract the user information from the token and return it")
        void whenCalled_shouldExtractTheUserInformationFromTheToken_andExtractIt() {
            String token = tokenManager.generateTokenForUser(user);

            User extractedUser = tokenManager.extractUserFromToken(token);

            assertThat(extractedUser.getId()).isEqualTo(user.getId());
            assertThat(extractedUser.getName()).isEqualTo(user.getName());
            assertThat(extractedUser.getEmail()).isEqualTo(user.getEmail());
        }
    }
}