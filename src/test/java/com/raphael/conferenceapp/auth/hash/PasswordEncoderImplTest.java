package com.raphael.conferenceapp.auth.hash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderImplTest {
    private PasswordEncoderImpl encoder;

    @BeforeEach
    void setUp() {
        this.encoder = new PasswordEncoderImpl();

    }

    @Nested
    @DisplayName("method: hashPassword(String)")
    class HashPasswordMethod {

        @Test
        @DisplayName("when called, then it should generate a hash for the password")
        void whenCalled_shouldGenerateAHashForThePassword() {
            String password = "some-password";
            String hashedPassword = encoder.hashPassword(password);

            assertThat(hashedPassword).hasSize(60);
        }
    }

    @Nested
    @DisplayName("method: comparePasswordAndHash(String, String)")
    class ComparePasswordAndHashMethod {

        @Test
        @DisplayName("when called with correct password, then it should return true")
        void whenCalledWithCorrectPassword_shouldReturnTrue() {
            String hashedPassword = "$2a$10$My4G4zKLU6ff1CxU5ICER.ba46Qsp0nBqbL5VRSEzCR7uPRs/4JJ2";
            String password = "some-password";

            assertThat(encoder.comparePasswordAndHash(password, hashedPassword)).isTrue();
        }

        @Test
        @DisplayName("when called with incorrect password, then it should return false")
        void whenCalledWithIncorrectPassword_shouldReturnFalse() {
            String hashedPassword = "$2a$10$My4G4zKLU6ff1CxU5ICER.ba46Qsp0nBqbL5VRSEzCR7uPRs/4JJ2";
            String password = "some-password2";

            assertThat(encoder.comparePasswordAndHash(password, hashedPassword)).isFalse();
        }
    }
}