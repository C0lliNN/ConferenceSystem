package com.raphael.conferenceapp.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.auth.mock.AuthMock;
import com.raphael.conferenceapp.auth.usecase.PasswordEncoder;
import com.raphael.conferenceapp.auth.usecase.UserRepository;
import com.raphael.conferenceapp.auth.usecase.request.LoginRequest;
import com.raphael.conferenceapp.auth.usecase.request.RegisterRequest;
import com.raphael.conferenceapp.initializer.DatabaseContainerInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@EntityScan(basePackages = "com.raphael.conferenceapp.*")
@EnableJpaRepositories(basePackages = "com.raphael.conferenceapp.*")
@ContextConfiguration(initializers = DatabaseContainerInitializer.class)
class AuthControllerTest {
    private static final String BASE_URL = "/auth";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Nested
    @DisplayName("login(LoginRequest)")
    class LoginMethod{
        private final String EMAIL = Faker.instance().internet().emailAddress();
        private final String PASSWORD = Faker.instance().internet().password(8, 16);

        private User user;

        @BeforeEach
        void setUp() {
            user = User.builder()
                    .name(Faker.instance().name().firstName())
                    .email(EMAIL)
                    .password(passwordEncoder.hashPassword(PASSWORD))
                    .build();

            user = repository.save(user);
        }

        @Test
        @DisplayName("when called with unknown email, then it should 401 error")
        void whenCalledWithUnknownEmail_shouldReturn401Error() throws Exception {
            LoginRequest payload = new LoginRequest("test@test.com", PASSWORD);

            MockHttpServletRequestBuilder request = post(BASE_URL + "/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("The email 'test@test.com' could not be found."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when called with incorrect password, then it should 401 error")
        void whenCalledWithIncorrectPassword_shouldReturn401Error() throws Exception {
            LoginRequest payload = new LoginRequest(EMAIL, "password");

            MockHttpServletRequestBuilder request = post(BASE_URL + "/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("The provided password is incorrect."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when called with valid credentials, then it should 200 and the token")
        void whenCalledWithValidCredentials_shouldReturn200AndTheToken() throws Exception {
            LoginRequest payload = new LoginRequest(EMAIL, PASSWORD);

            MockHttpServletRequestBuilder request = post(BASE_URL + "/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(user.getId()))
                    .andExpect(jsonPath("$.name").value(user.getName()))
                    .andExpect(jsonPath("$.email").value(user.getEmail()))
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }
    }

    @Nested
    @DisplayName("method: register(RegisterRequest)")
    class RegisterMethod {

        @Test
        @DisplayName("when called valid data, then it should return 201 and persist the user")
        void whenCalledWithValidData_shouldReturn201AndPersistTheUser() throws Exception {
            RegisterRequest payload = new RegisterRequest(
                    Faker.instance().name().firstName(),
                    Faker.instance().internet().emailAddress(),
                    Faker.instance().internet().password(8, 12)
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.name").value(payload.name()))
                    .andExpect(jsonPath("$.email").value(payload.email()))
                    .andExpect(jsonPath("$.token").isNotEmpty());

            assertThat(repository.findByEmail(payload.email())).isNotEmpty();
        }

        @Test
        @DisplayName("when called with existing email, then it should return 409 error")
        void whenCalledWithExistingEmail_shouldReturn409Error() throws Exception {
            User user = AuthMock.newUserDomain();
            repository.save(user);

            RegisterRequest payload = new RegisterRequest(
                    Faker.instance().name().firstName(),
                    user.getEmail(),
                    Faker.instance().internet().password(8, 12)
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("This email is already being used."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }
    }
}