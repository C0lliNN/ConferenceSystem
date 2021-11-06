package com.raphael.conferenceapp.auth.usecase;

import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.auth.exception.EmailNotFoundException;
import com.raphael.conferenceapp.auth.exception.IncorrectPasswordException;
import com.raphael.conferenceapp.auth.usecase.request.LoginRequest;
import com.raphael.conferenceapp.auth.usecase.request.RegisterRequest;
import com.raphael.conferenceapp.auth.usecase.response.UserResponse;
import com.raphael.conferenceapp.auth.mock.AuthMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @InjectMocks
    private AuthUseCase useCase;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenGenerator tokenGenerator;

    @Nested
    @DisplayName("method: login(LoginRequest)")
    class LoginMethod {

        @AfterEach
        void tearDown() {
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("when user is not found, then it should throw an EmailNotFoundException")
        void whenUserIsNotFound_shouldThrowAnEmailNotFoundException() {
            LoginRequest request = AuthMock.newLoginRequest();

            when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.login(request))
                    .isInstanceOf(EmailNotFoundException.class)
                    .hasMessage("The email '%s' could not be found.", request.getEmail());

            verify(repository).findByEmail(request.getEmail());
            verifyNoInteractions(passwordEncoder, tokenGenerator);
        }

        @Test
        @DisplayName("when password is incorrect, then it should throw an IncorrectPasswordException")
        void whenPasswordIsIncorrect_shouldThrowAnIncorrectPasswordException() {
            LoginRequest request = AuthMock.newLoginRequest();
            User user = AuthMock.newUserDomain();

            when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
            when(passwordEncoder.comparePasswordAndHash(request.getPassword(), user.getPassword())).thenReturn(false);

            assertThatThrownBy(() -> useCase.login(request))
                    .isInstanceOf(IncorrectPasswordException.class)
                    .hasMessage("The provided password is incorrect.");


            verify(repository).findByEmail(request.getEmail());
            verify(passwordEncoder).comparePasswordAndHash(request.getPassword(), user.getPassword());

            verifyNoMoreInteractions(passwordEncoder);
            verifyNoInteractions(tokenGenerator);
        }

        @Test
        @DisplayName("when password is correct, then it should return a UserResponse")
        void whenPasswordIsCorrect_shouldReturnAUserResponse() {
            LoginRequest request = AuthMock.newLoginRequest();
            User user = AuthMock.newUserDomain();
            String token = "some token";

            when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
            when(passwordEncoder.comparePasswordAndHash(request.getPassword(), user.getPassword())).thenReturn(true);
            when(tokenGenerator.generateTokenForUser(user)).thenReturn(token);

            UserResponse expected = UserResponse.fromUser(user).withToken(token);
            UserResponse actual = useCase.login(request);

            assertThat(actual).isEqualTo(expected);

            verify(repository).findByEmail(request.getEmail());
            verify(passwordEncoder).comparePasswordAndHash(request.getPassword(), user.getPassword());
            verify(tokenGenerator).generateTokenForUser(user);

            verifyNoMoreInteractions(passwordEncoder, tokenGenerator);
        }
    }

    @Nested
    @DisplayName("method: register(RegisterRequest)")
    class RegisterMethod {

        @Test
        @DisplayName("when called, then it should register a new user")
        void whenCalled_shouldRegisterANewUser() {
            RegisterRequest request = AuthMock.newRegisterRequest();
            User user = request.toUser().withPassword("some-hashed-password");

            when(passwordEncoder.hashPassword(request.getPassword())).thenReturn("some-hashed-password");
            when(repository.save(user)).thenReturn(user);
            when(tokenGenerator.generateTokenForUser(user)).thenReturn("new-token");

            UserResponse expected = UserResponse.fromUser(user).withToken("new-token");
            UserResponse actual = useCase.register(request);

            assertThat(actual).isEqualTo(expected);

            verify(passwordEncoder).hashPassword(request.getPassword());
            verify(repository).save(user);
            verify(tokenGenerator).generateTokenForUser(user);

            verifyNoMoreInteractions(passwordEncoder, repository, tokenGenerator);
        }
    }
}