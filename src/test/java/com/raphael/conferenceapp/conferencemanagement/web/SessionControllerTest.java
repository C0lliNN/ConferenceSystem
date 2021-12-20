package com.raphael.conferenceapp.conferencemanagement.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphael.conferenceapp.conferencemanagement.entity.Session;
import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;
import com.raphael.conferenceapp.conferencemanagement.mock.SessionMock;
import com.raphael.conferenceapp.conferencemanagement.mock.SpeakerMock;
import com.raphael.conferenceapp.conferencemanagement.persistence.SqlSessionRepository;
import com.raphael.conferenceapp.conferencemanagement.persistence.SqlSpeakerRepository;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateSessionRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateSessionRequest;
import com.raphael.conferenceapp.utils.initializer.DatabaseContainerInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@EntityScan(basePackages = "com.raphael.conferenceapp.*")
@EnableJpaRepositories(basePackages = "com.raphael.conferenceapp.*")
@ContextConfiguration(initializers = DatabaseContainerInitializer.class)
class SessionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private SqlSpeakerRepository speakerRepository;

    @Autowired
    private SqlSessionRepository sessionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT = "/admin";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Nested
    @DisplayName("method: getSessions(Long)")
    class GetSessionsMethod {
        private static final long CONFERENCE_ID = 3L;
        private Session session1;
        private Session session2;
        private Session session3;

        @BeforeEach
        void setUp() {
            this.session1 = SessionMock.newSessionDomain();
            this.session1 = session1
                    .toBuilder()
                    .speaker(speakerRepository.save(session1.getSpeaker()))
                    .conferenceId(CONFERENCE_ID)
                    .build();
            this.session1 = sessionRepository.save(session1);

            this.session2 = SessionMock.newSessionDomain();
            this.session2 = session2
                    .toBuilder()
                    .speaker(speakerRepository.save(session2.getSpeaker()))
                    .conferenceId(CONFERENCE_ID + 1)
                    .build();
            this.session2 = sessionRepository.save(session2);

            this.session3 = SessionMock.newSessionDomain();
            this.session3 = session3
                    .toBuilder()
                    .speaker(speakerRepository.save(session3.getSpeaker()))
                    .conferenceId(CONFERENCE_ID)
                    .build();
            this.session3 = sessionRepository.save(session3);
        }

        @Test
        @DisplayName("when called, then it should return all the session matching the given conferenceId")
        void whenCalled_shouldReturnAllTheSessionsMatchingTheGivenConferenceId() throws Exception {
            mockMvc.perform(get(ENDPOINT + "/conferences/{id}/sessions", CONFERENCE_ID))
                    .andDo(print())
                    // session 1
                    .andExpect(jsonPath("$[0].id").value(session1.getId()))
                    .andExpect(jsonPath("$[0].startTime").value(session1.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$[0].endTime").value(session1.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$[0].title").value(session1.getTitle()))
                    .andExpect(jsonPath("$[0].description").value(session1.getDescription()))
                    .andExpect(jsonPath("$[0].accessLink").value(session1.getAccessLink()))
                    .andExpect(jsonPath("$[0].speaker.id").value(session1.getSpeaker().getId()))
                    .andExpect(jsonPath("$[0].speaker.firstName").value(session1.getSpeaker().getFirstName()))
                    .andExpect(jsonPath("$[0].speaker.lastName").value(session1.getSpeaker().getLastName()))
                    .andExpect(jsonPath("$[0].speaker.email").value(session1.getSpeaker().getEmail()))
                    .andExpect(jsonPath("$[0].speaker.professionalTitle").value(session1.getSpeaker().getProfessionalTitle()))
                    // session 3
                    .andExpect(jsonPath("$[1].id").value(session3.getId()))
                    .andExpect(jsonPath("$[1].startTime").value(session3.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$[1].endTime").value(session3.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$[1].title").value(session3.getTitle()))
                    .andExpect(jsonPath("$[1].description").value(session3.getDescription()))
                    .andExpect(jsonPath("$[1].accessLink").value(session3.getAccessLink()))
                    .andExpect(jsonPath("$[1].speaker.id").value(session3.getSpeaker().getId()))
                    .andExpect(jsonPath("$[1].speaker.firstName").value(session3.getSpeaker().getFirstName()))
                    .andExpect(jsonPath("$[1].speaker.lastName").value(session3.getSpeaker().getLastName()))
                    .andExpect(jsonPath("$[1].speaker.email").value(session3.getSpeaker().getEmail()))
                    .andExpect(jsonPath("$[1].speaker.professionalTitle").value(session3.getSpeaker().getProfessionalTitle()));
        }
    }

    @Nested
    @DisplayName("method: createSession(CreateSessionRequest)")
    class CreateSessionMethod {
        private Speaker speaker;

        @BeforeEach
        void setUp() {
            this.speaker = speakerRepository.save(SpeakerMock.newSpeakerDomain());
        }

        @Test
        @DisplayName("when called without title, then it should return a 400 error")
        void whenCalledWithoutTitle_shouldReturn400Error() throws Exception {
            CreateSessionRequest createSession = new CreateSessionRequest(
                    "",
                    "some long description",
                    LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                    LocalDateTime.now().plus(2, ChronoUnit.DAYS),
                    "https://google.com",
                    speaker.getId(),
                    2L
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("title"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called without description, then it should return a 400 error")
        void whenCalledWithoutDescription_shouldReturn400Error() throws Exception {
            CreateSessionRequest createSession = new CreateSessionRequest(
                    "Spring MVC vs Spring Webflux",
                    null,
                    LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                    LocalDateTime.now().plus(2, ChronoUnit.DAYS),
                    "https://google.com",
                    speaker.getId(),
                    2L
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("description"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called without startTime, then it should return a 400 error")
        void whenCalledWithoutStartTime_shouldReturn400Error() throws Exception {
            CreateSessionRequest createSession = new CreateSessionRequest(
                    "Spring MVC vs Spring Webflux",
                    "some long description",
                    null,
                    LocalDateTime.now().plus(2, ChronoUnit.DAYS),
                    "https://google.com",
                    speaker.getId(),
                    2L
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("startTime"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called without endTime, then it should return a 400 error")
        void whenCalledWithoutEndTime_shouldReturn400Error() throws Exception {
            CreateSessionRequest createSession = new CreateSessionRequest(
                    "Spring MVC vs Spring Webflux",
                    "some long description",
                    LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                    null,
                    "https://google.com",
                    speaker.getId(),
                    2L
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("endTime"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called without accessLink, then it should return a 400 error")
        void whenCalledWithoutAccessLink_shouldReturn400Error() throws Exception {
            CreateSessionRequest createSession = new CreateSessionRequest(
                    "Spring MVC vs Spring Webflux",
                    "some long description",
                    LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                    LocalDateTime.now().plus(2, ChronoUnit.DAYS),
                    null,
                    speaker.getId(),
                    2L
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("accessLink"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called without speakerId, then it should return a 400 error")
        void whenCalledWithoutSpeakerId_shouldReturn400Error() throws Exception {
            CreateSessionRequest createSession = new CreateSessionRequest(
                    "Spring MVC vs Spring Webflux",
                    "some long description",
                    LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                    LocalDateTime.now().plus(2, ChronoUnit.DAYS),
                    "https://google.com",
                    null,
                    2L
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("speakerId"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called without conferenceId, then it should return a 400 error")
        void whenCalledWithoutConferenceId_shouldReturn400Error() throws Exception {
            CreateSessionRequest createSession = new CreateSessionRequest(
                    "Spring MVC vs Spring Webflux",
                    "some long description",
                    LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                    LocalDateTime.now().plus(2, ChronoUnit.DAYS),
                    "https://google.com",
                    speaker.getId(),
                    null
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("conferenceId"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called with valid payload, then it should return 201 and persist the session")
        void whenCalledWithValidPayload_shouldReturn201AndPersistTheSession() throws Exception {
            CreateSessionRequest createSession = new CreateSessionRequest(
                    "Spring MVC vs Spring Webflux",
                    "some long description",
                    LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                    LocalDateTime.now().plus(2, ChronoUnit.DAYS),
                    "https://google.com",
                    speaker.getId(),
                    3L
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value("Spring MVC vs Spring Webflux"))
                    .andExpect(jsonPath("$.description").value("some long description"));

            assertThat(sessionRepository.findByConferenceId(3L)).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("method: updateSession(Long, UpdateSessionRequest)")
    class UpdateSessionMethod {
        private Session session;

        @BeforeEach
        void setUp() {
            this.session = SessionMock.newSessionDomain();
            this.session = session.toBuilder()
                    .speaker(speakerRepository.save(session.getSpeaker()))
                    .build();
            this.session = sessionRepository.save(session);
        }

        @Test
        @DisplayName("when called with unknown id, then it should return a 404 error")
        void whenCalledWithUnknownId_shouldReturn404Error() throws Exception {
            UpdateSessionRequest updateSession = new UpdateSessionRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            MockHttpServletRequestBuilder request = patch(ENDPOINT + "/sessions/{id}", 100L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Session with ID 100 was not found."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when called with invalid payload, then it should return 400 error")
        void whenCalledWithInvalidPayload_shouldReturn400EError() throws Exception {
            UpdateSessionRequest updateSession = new UpdateSessionRequest(
                    UUID.randomUUID().toString().repeat(5),
                    null,
                    null,
                    null,
                    "invalid link",
                    null
            );

            MockHttpServletRequestBuilder request = patch(ENDPOINT + "/sessions/{id}", session.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("accessLink"))
                    .andExpect(jsonPath("$.details[0].message").value("the field must contain a valid URL"))
                    .andExpect(jsonPath("$.details[1].field").value("title"))
                    .andExpect(jsonPath("$.details[1].message").value("the field must contain at most 150 characters"));
        }

        @Test
        @DisplayName("when called with title, then it should update it")
        void whenCalledWithTitle_shouldUpdateIt() throws Exception {
            UpdateSessionRequest updateSession = new UpdateSessionRequest(
                    "New title",
                    null,
                    null,
                    null,
                    null,
                    null
            );

            MockHttpServletRequestBuilder request = patch(ENDPOINT + "/sessions/{id}", session.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(sessionRepository.findById(session.getId()).orElseThrow().getTitle()).isEqualTo("New title");
        }

        @Test
        @DisplayName("when called with description, then it should update it")
        void whenCalledWithDescription_shouldUpdateIt() throws Exception {
            UpdateSessionRequest updateSession = new UpdateSessionRequest(
                    null,
                    "New description",
                    null,
                    null,
                    null,
                    null
            );

            MockHttpServletRequestBuilder request = patch(ENDPOINT + "/sessions/{id}", session.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(sessionRepository.findById(session.getId()).orElseThrow().getDescription()).isEqualTo("New description");
        }

        @Test
        @DisplayName("when called with startTime, then it should update it")
        void whenCalledWithStartTime_shouldUpdateIt() throws Exception {
            LocalDateTime startTime = LocalDateTime.of(2021, Month.DECEMBER, 18, 5, 0, 0);
            UpdateSessionRequest updateSession = new UpdateSessionRequest(
                    null,
                    null,
                    startTime,
                    null,
                    null,
                    null
            );

            MockHttpServletRequestBuilder request = patch(ENDPOINT + "/sessions/{id}", session.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(sessionRepository.findById(session.getId()).orElseThrow().getStartTime()).isEqualTo(startTime);
        }

        @Test
        @DisplayName("when called with endTime, then it should update it")
        void whenCalledWithEndTime_shouldUpdateIt() throws Exception {
            LocalDateTime endTime = LocalDateTime.of(2021, Month.DECEMBER, 18, 5, 0, 0);
            UpdateSessionRequest updateSession = new UpdateSessionRequest(
                    null,
                    null,
                    null,
                    endTime,
                    null,
                    null
            );

            MockHttpServletRequestBuilder request = patch(ENDPOINT + "/sessions/{id}", session.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(sessionRepository.findById(session.getId()).orElseThrow().getEndTime()).isEqualTo(endTime);
        }

        @Test
        @DisplayName("when called with accessLink, then it should update it")
        void whenCalledWithAccessLink_shouldUpdateIt() throws Exception {
            UpdateSessionRequest updateSession = new UpdateSessionRequest(
                    null,
                    null,
                    null,
                    null,
                    "https://google.com",
                    null
            );

            MockHttpServletRequestBuilder request = patch(ENDPOINT + "/sessions/{id}", session.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(sessionRepository.findById(session.getId()).orElseThrow().getAccessLink()).isEqualTo("https://google.com");
        }

        @Test
        @DisplayName("when called with speakerId, then it should update it")
        void whenCalledWithSpeakerId_shouldUpdateIt() throws Exception {
            Speaker speaker = speakerRepository.save(SpeakerMock.newSpeakerDomain());
            UpdateSessionRequest updateSession = new UpdateSessionRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    speaker.getId()
            );

            MockHttpServletRequestBuilder request = patch(ENDPOINT + "/sessions/{id}", session.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSession));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(sessionRepository.findById(session.getId()).orElseThrow().getSpeaker()).isEqualTo(speaker);
        }
    }

    @Nested
    @DisplayName("method: deleteSession(Long)")
    class DeleteSessionMethod {
        private Session session;

        @BeforeEach
        void setUp() {
            this.session = SessionMock.newSessionDomain();
            this.session = session.toBuilder()
                    .speaker(speakerRepository.save(session.getSpeaker()))
                    .build();
            this.session = sessionRepository.save(session);
        }

        @Test
        @DisplayName("when called with unknown id, then it should return 404 error")
        void whenCalledWithUnknownId_shouldReturn404Error() throws Exception {
            mockMvc.perform(delete(ENDPOINT + "/sessions/{id}", 100L))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Session with ID 100 was not found."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when called with existing id, then it should return 200 and delete the matching session")
        void whenCalledWithExistingId_shouldReturn200AndDeleteTheMatchingSession() throws Exception {
            mockMvc.perform(delete(ENDPOINT + "/sessions/{id}", session.getId()))
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(sessionRepository.findById(session.getId())).isEmpty();
        }
    }
}