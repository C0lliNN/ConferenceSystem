package com.raphael.conferenceapp.auth.usecase;

import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.auth.exception.EmailNotFoundException;
import com.raphael.conferenceapp.auth.exception.IncorrectPasswordException;
import com.raphael.conferenceapp.auth.usecase.request.LoginRequest;
import com.raphael.conferenceapp.auth.usecase.request.RegisterRequest;
import com.raphael.conferenceapp.auth.usecase.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthUseCase {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public UserResponse login(LoginRequest request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("The email '%s' could not be found.", request.getEmail()));

        if (!passwordEncoder.comparePasswordAndHash(request.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("The provided password is incorrect.");
        }

        return createUserResponseWithToken(user);
    }

    public UserResponse register(RegisterRequest request) {
        User user = request.toUser();

        user = user.withPassword(passwordEncoder.hashPassword(user.getPassword()));
        user = repository.save(user);

        return createUserResponseWithToken(user);
    }

    private UserResponse createUserResponseWithToken(User user) {
        String token = tokenGenerator.generateTokenForUser(user);

        return UserResponse.fromUser(user).withToken(token);
    }
}
