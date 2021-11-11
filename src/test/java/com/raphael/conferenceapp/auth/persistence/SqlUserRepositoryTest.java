package com.raphael.conferenceapp.auth.persistence;

import com.raphael.conferenceapp.initializer.DatabaseContainerInitializer;
import com.raphael.conferenceapp.auth.entity.User;
import com.raphael.conferenceapp.auth.exception.DuplicateEmailException;
import com.raphael.conferenceapp.config.DatabaseTestAutoConfiguration;
import com.raphael.conferenceapp.auth.mock.AuthMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@EnableAutoConfiguration
@SpringBootTest(
        classes = {
                DatabaseTestAutoConfiguration.class,
                SqlUserRepository.class,
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ContextConfiguration(initializers = DatabaseContainerInitializer.class)
class SqlUserRepositoryTest {
    @Autowired
    private SqlUserRepository repository;

    @Nested
    @DisplayName("method: findByEmail(String)")
    class FindByEmailMethod {

        @Test
        @DisplayName("when called with unknown email, then it should return optional empty")
        void whenCalledWithUnknownEmail_shouldReturnOptionalEmpty() {
            String email = "some_test@test.com";

            assertThat(repository.findByEmail(email)).isEmpty();
        }

        @Test
        @DisplayName("when called with existing email, then it should return the matching user wrapped into an Optional")
        void whenCalledWithExistingEmail_shouldReturnTheMatchingUserWrappedIntoAnOptional() {
            User user = AuthMock.newUserDomain();

            user = repository.save(user);

            assertThat(repository.findByEmail(user.getEmail())).hasValue(user);
        }
    }

    @Nested
    @DisplayName("method: save(User)")
    class SaveMethod {

        @Test
        @DisplayName("when the user email is already being used, then it should throw a DuplicateEmailException")
        void whenTheUserEmailIsAlreadyBeingUsed_shouldThrowADuplicateEmailException() {
            User user = AuthMock.newUserDomain();

            repository.save(user);

            assertThatThrownBy(() -> repository.save(user))
                    .isInstanceOf(DuplicateEmailException.class)
                    .hasMessage("This email is already being used.");
        }

        @Test
        @DisplayName("when no conflict occurs, then it should persist the user successfully")
        void whenNoConflictOccurs_shouldPersistTheUserSuccessfully() {
            User user = AuthMock.newUserDomain();

            User expected = repository.save(user);
            User actual = repository.findByEmail(user.getEmail()).orElseThrow();

            assertThat(actual).isEqualTo(expected);
        }
    }
}