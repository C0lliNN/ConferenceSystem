package com.raphael.conferenceapp.conferencemanagement.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.entity.ConferenceQuery;
import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.entity.Session;
import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;
import com.raphael.conferenceapp.conferencemanagement.entity.SpeakerQuery;
import com.raphael.conferenceapp.conferencemanagement.mock.ConferenceMock;
import com.raphael.conferenceapp.conferencemanagement.mock.ParticipantMock;
import com.raphael.conferenceapp.conferencemanagement.mock.SessionMock;
import com.raphael.conferenceapp.conferencemanagement.mock.SpeakerMock;
import com.raphael.conferenceapp.conferencemanagement.persistence.JpaParticipantRepository;
import com.raphael.conferenceapp.conferencemanagement.persistence.ParticipantEntity;
import com.raphael.conferenceapp.conferencemanagement.persistence.SqlConferenceRepository;
import com.raphael.conferenceapp.conferencemanagement.persistence.SqlSessionRepository;
import com.raphael.conferenceapp.conferencemanagement.persistence.SqlSpeakerRepository;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateConferenceRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateSessionRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateSpeakerRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateSessionRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateSpeakerRequest;
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
import java.util.List;
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
class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private SqlSpeakerRepository speakerRepository;

    @Autowired
    private SqlSessionRepository sessionRepository;

    @Autowired
    private SqlConferenceRepository conferenceRepository;

    @Autowired
    private JpaParticipantRepository jpaParticipantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT = "/admin";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Nested
    @DisplayName("method: getConferences(SearchConferences)")
    class GetConferencesMethod {
        private Conference conference1;
        private Conference conference2;
        private Conference conference3;

        private Session session1;
        private Session session2;

        @BeforeEach
        void setUp() {
            this.conference1 = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .startTime(LocalDateTime.of(2021, Month.NOVEMBER, 22, 15, 50))
                    .build();
            this.conference1 = conferenceRepository.save(conference1);

            this.conference2 = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .startTime(LocalDateTime.of(2021, Month.NOVEMBER, 21, 15, 50))
                    .build();
            this.conference2 = conferenceRepository.save(conference2);

            this.conference3 = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .startTime(LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 50))
                    .build();
            this.conference3 = conferenceRepository.save(conference3);

            this.session1 = SessionMock.newSessionDomain();
            this.session1 = session1.toBuilder()
                    .speaker(speakerRepository.save(session1.getSpeaker()))
                    .conferenceId(conference1.getId())
                    .build();
            this.session1 = sessionRepository.save(session1);

            this.session2 = SessionMock.newSessionDomain();
            this.session2 = session2.toBuilder()
                    .speaker(speakerRepository.save(session2.getSpeaker()))
                    .conferenceId(conference2.getId())
                    .build();
            this.session2 = sessionRepository.save(session2);
        }

        @Test
        @DisplayName("when called without query params, then it should return all the items")
        void whenCalledWithoutQueryParams_shouldReturnAllItems() throws Exception {
            mockMvc.perform(get(ENDPOINT + "/conferences"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.totalItems").value(3L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.results[0].id").value(conference1.getId()))
                    .andExpect(jsonPath("$.results[0].title").value(conference1.getTitle()))
                    .andExpect(jsonPath("$.results[0].description").value(conference1.getDescription()))
                    .andExpect(jsonPath("$.results[0].startTime").value(conference1.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].endTime").value(conference1.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].totalParticipants").value(conference1.getTotalParticipants()))
                    .andExpect(jsonPath("$.results[0].participantLimit").value(conference1.getParticipantLimit()))
                    .andExpect(jsonPath("$.results[0].userId").value(conference1.getUserId()))
                    .andExpect(jsonPath("$.results[1].id").value(conference2.getId()))
                    .andExpect(jsonPath("$.results[1].title").value(conference2.getTitle()))
                    .andExpect(jsonPath("$.results[1].description").value(conference2.getDescription()))
                    .andExpect(jsonPath("$.results[1].startTime").value(conference2.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[1].endTime").value(conference2.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[1].totalParticipants").value(conference2.getTotalParticipants()))
                    .andExpect(jsonPath("$.results[1].participantLimit").value(conference2.getParticipantLimit()))
                    .andExpect(jsonPath("$.results[1].userId").value(conference2.getUserId()))
                    .andExpect(jsonPath("$.results[2].id").value(conference3.getId()))
                    .andExpect(jsonPath("$.results[2].title").value(conference3.getTitle()))
                    .andExpect(jsonPath("$.results[2].description").value(conference3.getDescription()))
                    .andExpect(jsonPath("$.results[2].startTime").value(conference3.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[2].endTime").value(conference3.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[2].totalParticipants").value(conference3.getTotalParticipants()))
                    .andExpect(jsonPath("$.results[2].participantLimit").value(conference3.getParticipantLimit()))
                    .andExpect(jsonPath("$.results[2].userId").value(conference3.getUserId()))
                    .andExpect(jsonPath("$.results[2].sessions").isEmpty());
        }

        @Test
        @DisplayName("when called with title query param, then it should return only the matching items")
        void whenCalledWithTitleQueryParam_shouldReturnOnlyTheMatchingItems() throws Exception {
            MockHttpServletRequestBuilder request = get(ENDPOINT + "/conferences")
                    .queryParam("title", conference2.getTitle());

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.totalItems").value(1L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.results[0].id").value(conference2.getId()))
                    .andExpect(jsonPath("$.results[0].title").value(conference2.getTitle()))
                    .andExpect(jsonPath("$.results[0].description").value(conference2.getDescription()))
                    .andExpect(jsonPath("$.results[0].startTime").value(conference2.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].endTime").value(conference2.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].totalParticipants").value(conference2.getTotalParticipants()))
                    .andExpect(jsonPath("$.results[0].participantLimit").value(conference2.getParticipantLimit()))
                    .andExpect(jsonPath("$.results[0].userId").value(conference2.getUserId()));
        }

        @Test
        @DisplayName("when called with startTime query param, then it should return only the matching items")
        void whenCalledWithStartTimeQueryParam_shouldReturnOnlyTheMatchingItems() throws Exception {
            MockHttpServletRequestBuilder request = get(ENDPOINT + "/conferences")
                    .queryParam("startTime", LocalDateTime.of(2021, Month.NOVEMBER, 22, 15, 50).format(DateTimeFormatter.ISO_DATE_TIME));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.totalItems").value(1L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.results[0].id").value(conference1.getId()))
                    .andExpect(jsonPath("$.results[0].title").value(conference1.getTitle()))
                    .andExpect(jsonPath("$.results[0].description").value(conference1.getDescription()))
                    .andExpect(jsonPath("$.results[0].startTime").value(conference1.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].endTime").value(conference1.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].totalParticipants").value(conference1.getTotalParticipants()))
                    .andExpect(jsonPath("$.results[0].participantLimit").value(conference1.getParticipantLimit()))
                    .andExpect(jsonPath("$.results[0].userId").value(conference1.getUserId()));
        }

        @Test
        @DisplayName("when called with userId query param, then it should return only the matching items")
        void whenCalledWithUserIdQueryParam_shouldReturnOnlyTheMatchingItems() throws Exception {
            MockHttpServletRequestBuilder request = get(ENDPOINT + "/conferences")
                    .queryParam("userId", conference2.getUserId().toString());

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.totalItems").value(1L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.results[0].id").value(conference2.getId()))
                    .andExpect(jsonPath("$.results[0].title").value(conference2.getTitle()))
                    .andExpect(jsonPath("$.results[0].description").value(conference2.getDescription()))
                    .andExpect(jsonPath("$.results[0].startTime").value(conference2.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].endTime").value(conference2.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].participantLimit").value(conference2.getParticipantLimit()))
                    .andExpect(jsonPath("$.results[0].totalParticipants").value(conference2.getTotalParticipants()))
                    .andExpect(jsonPath("$.results[0].userId").value(conference2.getUserId()));
        }
    }

    @Nested
    @DisplayName("method: getParticipantsForConferenceId(Long, SearchParticipantsRequest)")
    class GetParticipantsForConferenceIdMethod {
        private Participant participant1;
        private Participant participant2;
        private Participant participant3;

        @BeforeEach
        void setUp() {
            this.participant1 = ParticipantMock.newParticipantDomain().toBuilder().conferenceId(1L).build();
            this.participant1 = jpaParticipantRepository.save(ParticipantEntity.fromDomain(participant1)).toDomain();

            this.participant2 = ParticipantMock.newParticipantDomain().toBuilder().conferenceId(2L).build();
            this.participant2 = jpaParticipantRepository.save(ParticipantEntity.fromDomain(participant2)).toDomain();

            this.participant3 = ParticipantMock.newParticipantDomain().toBuilder().conferenceId(1L).build();
            this.participant3 = jpaParticipantRepository.save(ParticipantEntity.fromDomain(participant3)).toDomain();
        }

        @Test
        @DisplayName("when called without query params, then it should return all the matching items")
        void whenCalledWithoutQueryParams_shouldReturnAllTheMatchingItems() throws Exception {
            mockMvc.perform(get(ENDPOINT + "/conferences/{id}/participants", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.totalItems").value(2L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.results[0].id").value(participant1.getId()))
                    .andExpect(jsonPath("$.results[0].name").value(participant1.getName()))
                    .andExpect(jsonPath("$.results[0].email").value(participant1.getEmail()))
                    .andExpect(jsonPath("$.results[0].subscribedAt").value(participant1.getSubscribedAt().toString()))
                    .andExpect(jsonPath("$.results[0].conferenceId").value(participant1.getConferenceId()))
                    .andExpect(jsonPath("$.results[1].id").value(participant3.getId()))
                    .andExpect(jsonPath("$.results[1].name").value(participant3.getName()))
                    .andExpect(jsonPath("$.results[1].email").value(participant3.getEmail()))
                    .andExpect(jsonPath("$.results[1].subscribedAt").value(participant3.getSubscribedAt().toString()))
                    .andExpect(jsonPath("$.results[1].conferenceId").value(participant3.getConferenceId()));
        }

        @Test
        @DisplayName("when called with title query param, then it should return all the matching items")
        void whenCalledWithTitleQueryParam_shouldReturnAllTheMatchingItems() throws Exception {
            MockHttpServletRequestBuilder request = get(ENDPOINT + "/conferences/{id}/participants", 1L)
                    .queryParam("name", participant1.getName());

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.totalItems").value(1L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.results[0].id").value(participant1.getId()))
                    .andExpect(jsonPath("$.results[0].name").value(participant1.getName()))
                    .andExpect(jsonPath("$.results[0].email").value(participant1.getEmail()))
                    .andExpect(jsonPath("$.results[0].subscribedAt").value(participant1.getSubscribedAt().toString()))
                    .andExpect(jsonPath("$.results[0].conferenceId").value(participant1.getConferenceId()));
        }

        @Test
        @DisplayName("when called with email query param, then it should return all the matching items")
        void whenCalledWithEmailQueryParam_shouldReturnAllTheMatchingItems() throws Exception {
            MockHttpServletRequestBuilder request = get(ENDPOINT + "/conferences/{id}/participants", 1L)
                    .queryParam("email", participant1.getEmail());

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.totalItems").value(1L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.results[0].id").value(participant1.getId()))
                    .andExpect(jsonPath("$.results[0].name").value(participant1.getName()))
                    .andExpect(jsonPath("$.results[0].email").value(participant1.getEmail()))
                    .andExpect(jsonPath("$.results[0].subscribedAt").value(participant1.getSubscribedAt().toString()))
                    .andExpect(jsonPath("$.results[0].conferenceId").value(participant1.getConferenceId()));
        }

        @Test
        @DisplayName("when called with page and perPage query param, then it should return all the matching items")
        void whenCalledWithPageAndPerPageQueryParam_shouldReturnAllTheMatchingItems() throws Exception {
            MockHttpServletRequestBuilder request = get(ENDPOINT + "/conferences/{id}/participants", 1L)
                    .queryParam("page", "2")
                    .queryParam("perPage", "1");

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(2L))
                    .andExpect(jsonPath("$.totalPages").value(2L))
                    .andExpect(jsonPath("$.totalItems").value(2L))
                    .andExpect(jsonPath("$.perPage").value(1L))
                    .andExpect(jsonPath("$.results[0].id").value(participant3.getId()))
                    .andExpect(jsonPath("$.results[0].name").value(participant3.getName()))
                    .andExpect(jsonPath("$.results[0].email").value(participant3.getEmail()))
                    .andExpect(jsonPath("$.results[0].subscribedAt").value(participant3.getSubscribedAt().toString()))
                    .andExpect(jsonPath("$.results[0].conferenceId").value(participant3.getConferenceId()));
        }
    }

    @Nested
    @DisplayName("method: getConference(Long)")
    class GetConferenceMethod {
        private Conference conference;
        private Session session;

        @BeforeEach
        void setUp() {
            this.conference = conferenceRepository.save(ConferenceMock.newConferenceDomain());

            this.session = SessionMock.newSessionDomain();
            this.session = session.toBuilder()
                    .speaker(speakerRepository.save(session.getSpeaker()))
                    .conferenceId(conference.getId())
                    .build();

            this.session = sessionRepository.save(session);

            this.conference = conference.toBuilder().sessions(List.of(session)).build();
        }

        @Test
        @DisplayName("when called with unknown id, then it should return a 404 error")
        void whenCalledWithUnknownId_shouldReturn404Error() throws Exception {
            mockMvc.perform(get(ENDPOINT + "/conferences/{id}", 100L))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Conference with ID 100 was not found."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when called with existing id, then it should return the matching conference")
        void whenCalledWithExistingId_shouldReturnTheMatchingConference() throws Exception {
            mockMvc.perform(get(ENDPOINT + "/conferences/{id}", conference.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(conference.getId()))
                    .andExpect(jsonPath("$.title").value(conference.getTitle()))
                    .andExpect(jsonPath("$.description").value(conference.getDescription()))
                    .andExpect(jsonPath("$.startTime").value(conference.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.endTime").value(conference.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.participantLimit").value(conference.getParticipantLimit()))
                    .andExpect(jsonPath("$.totalParticipants").value(conference.getTotalParticipants()))
                    .andExpect(jsonPath("$.userId").value(conference.getUserId()));
        }
    }

    @Nested
    @DisplayName("method: createConference(CreateConferenceRequest)")
    class CreateConferenceMethod {

        @Test
        @DisplayName("when called without title, then it should return a 400 error")
        void whenCalledWithoutTitle_shouldReturn400Error() throws Exception {
            CreateConferenceRequest createConference = new CreateConferenceRequest(
                    null,
                    "some long description",
                    LocalDateTime.of(2021, Month.DECEMBER, 19, 14, 30),
                    LocalDateTime.of(2021, Month.DECEMBER, 29, 14, 30),
                    150
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createConference));

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
            CreateConferenceRequest createConference = new CreateConferenceRequest(
                    "SpringOne",
                    "",
                    LocalDateTime.of(2021, Month.DECEMBER, 19, 14, 30),
                    LocalDateTime.of(2021, Month.DECEMBER, 29, 14, 30),
                    150
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createConference));

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
            CreateConferenceRequest createConference = new CreateConferenceRequest(
                    "SpringOne",
                    "some long description",
                    null,
                    LocalDateTime.of(2021, Month.DECEMBER, 19, 14, 30),
                    150
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createConference));

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
            CreateConferenceRequest createConference = new CreateConferenceRequest(
                    "SpringOne",
                    "some long description",
                    LocalDateTime.of(2021, Month.DECEMBER, 29, 14, 30),
                    null,
                    150
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createConference));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("endTime"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @WithCustomMockUser(id = 2L)
        @Test
        @DisplayName("when called with valid payload, then it should return a 201 and persist the conference")
        void whenCalledWithValidPayload_shouldReturn201AndPersistTheConference() throws Exception {
            CreateConferenceRequest createConference = new CreateConferenceRequest(
                    "SpringOne",
                    "some long description",
                    LocalDateTime.of(2021, Month.DECEMBER, 19, 14, 30),
                    LocalDateTime.of(2021, Month.DECEMBER, 29, 14, 30),
                    150
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createConference));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value(createConference.title()))
                    .andExpect(jsonPath("$.description").value(createConference.description()))
                    .andExpect(jsonPath("$.startTime").value(createConference.startTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.endTime").value(createConference.endTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.participantLimit").value(createConference.participantLimit()))
                    .andExpect(jsonPath("$.totalParticipants").value(0))
                    .andExpect(jsonPath("$.userId").value(2L));

            PaginatedItems<Conference> paginatedConferences = conferenceRepository.findByQuery(new ConferenceQuery(null, null, null, null, 10L, 0L));

            assertThat(paginatedConferences.items()).hasSize(1);
        }

        @WithCustomMockUser(id = 3L)
        @Test
        @DisplayName("when called without participantLimit, then it should return a 201 and persist the conference")
        void whenCalledWithoutParticipantLimit_shouldReturn201AndPersistTheConference() throws Exception {
            CreateConferenceRequest createConference = new CreateConferenceRequest(
                    "SpringOne",
                    "some long description",
                    LocalDateTime.of(2021, Month.DECEMBER, 19, 14, 30),
                    LocalDateTime.of(2021, Month.DECEMBER, 29, 14, 30),
                    null
            );

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createConference));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value(createConference.title()))
                    .andExpect(jsonPath("$.description").value(createConference.description()))
                    .andExpect(jsonPath("$.startTime").value(createConference.startTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.endTime").value(createConference.endTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.participantLimit").isEmpty())
                    .andExpect(jsonPath("$.totalParticipants").value(0))
                    .andExpect(jsonPath("$.userId").value(3L));

            PaginatedItems<Conference> paginatedConferences = conferenceRepository.findByQuery(new ConferenceQuery(null, null, null, null, 10L, 0L));

            assertThat(paginatedConferences.items()).hasSize(1);
        }
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

    @Nested
    @DisplayName("method: getSpeakers(SearchSpeakersRequest)")
    class GetSpeakersMethod {
        private Speaker speaker1;
        private Speaker speaker2;
        private Speaker speaker3;

        @BeforeEach
        void setUp() {
            this.speaker1 = SpeakerMock.newSpeakerDomain()
                    .toBuilder()
                    .id(null)
                    .firstName("Annie")
                    .build();
            this.speaker1 = speakerRepository.save(speaker1);

            this.speaker2 = SpeakerMock.newSpeakerDomain()
                    .toBuilder()
                    .id(null)
                    .firstName("Bob")
                    .build();
            this.speaker2 = speakerRepository.save(speaker2);

            this.speaker3 = SpeakerMock.newSpeakerDomain()
                    .toBuilder()
                    .id(null)
                    .firstName("John")
                    .build();
            this.speaker3 = speakerRepository.save(speaker3);
        }

        @Test
        @DisplayName("when called without query params, then it should return the persisted items")
        void whenCalledWithoutQueryParams_shouldReturnThePersistedItems() throws Exception {
            mockMvc.perform(get(ENDPOINT + "/speakers"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.totalItems").value(3L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.results[0].id").value(speaker1.getId()))
                    .andExpect(jsonPath("$.results[0].firstName").value(speaker1.getFirstName()))
                    .andExpect(jsonPath("$.results[0].lastName").value(speaker1.getLastName()))
                    .andExpect(jsonPath("$.results[0].email").value(speaker1.getEmail()))
                    .andExpect(jsonPath("$.results[0].professionalTitle").value(speaker1.getProfessionalTitle()))
                    .andExpect(jsonPath("$.results[1].id").value(speaker2.getId()))
                    .andExpect(jsonPath("$.results[1].firstName").value(speaker2.getFirstName()))
                    .andExpect(jsonPath("$.results[1].lastName").value(speaker2.getLastName()))
                    .andExpect(jsonPath("$.results[1].email").value(speaker2.getEmail()))
                    .andExpect(jsonPath("$.results[1].professionalTitle").value(speaker2.getProfessionalTitle()))
                    .andExpect(jsonPath("$.results[2].id").value(speaker3.getId()))
                    .andExpect(jsonPath("$.results[2].firstName").value(speaker3.getFirstName()))
                    .andExpect(jsonPath("$.results[2].lastName").value(speaker3.getLastName()))
                    .andExpect(jsonPath("$.results[2].email").value(speaker3.getEmail()))
                    .andExpect(jsonPath("$.results[2].professionalTitle").value(speaker3.getProfessionalTitle()));
        }

        @Test
        @DisplayName("when called with name query param, then it should return only the matching items")
        void whenCalledWithNameQueryParam_shouldReturnOnlyTheMatchingItems() throws Exception {
            MockHttpServletRequestBuilder request = get(ENDPOINT + "/speakers")
                    .queryParam("name", "Bob");

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.totalItems").value(1L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.results[0].id").value(speaker2.getId()))
                    .andExpect(jsonPath("$.results[0].firstName").value(speaker2.getFirstName()))
                    .andExpect(jsonPath("$.results[0].lastName").value(speaker2.getLastName()))
                    .andExpect(jsonPath("$.results[0].email").value(speaker2.getEmail()))
                    .andExpect(jsonPath("$.results[0].professionalTitle").value(speaker2.getProfessionalTitle()));
        }

        @Test
        @DisplayName("when called with email query param, then it should return only the matching items")
        void whenCalledWithEmailQueryParam_shouldReturnOnlyTheMatchingItems() throws Exception {
            MockHttpServletRequestBuilder request = get(ENDPOINT + "/speakers")
                    .queryParam("email", speaker2.getEmail());

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(1L))
                    .andExpect(jsonPath("$.perPage").value(10L))
                    .andExpect(jsonPath("$.totalItems").value(1L))
                    .andExpect(jsonPath("$.totalPages").value(1L))
                    .andExpect(jsonPath("$.results[0].id").value(speaker2.getId()))
                    .andExpect(jsonPath("$.results[0].firstName").value(speaker2.getFirstName()))
                    .andExpect(jsonPath("$.results[0].lastName").value(speaker2.getLastName()))
                    .andExpect(jsonPath("$.results[0].email").value(speaker2.getEmail()))
                    .andExpect(jsonPath("$.results[0].professionalTitle").value(speaker2.getProfessionalTitle()));
        }

        @Test
        @DisplayName("when called with page and perPage query params, then it should return only the matching items")
        void whenCalledWithPageAndPerPageQueryParams_shouldReturnOnlyTheMatchingItems() throws Exception {
            MockHttpServletRequestBuilder request = get(ENDPOINT + "/speakers")
                    .queryParam("perPage", "1")
                    .queryParam("page", "2");

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(2L))
                    .andExpect(jsonPath("$.perPage").value(1L))
                    .andExpect(jsonPath("$.totalItems").value(3L))
                    .andExpect(jsonPath("$.totalPages").value(3L))
                    .andExpect(jsonPath("$.results[0].id").value(speaker2.getId()))
                    .andExpect(jsonPath("$.results[0].firstName").value(speaker2.getFirstName()))
                    .andExpect(jsonPath("$.results[0].lastName").value(speaker2.getLastName()))
                    .andExpect(jsonPath("$.results[0].email").value(speaker2.getEmail()))
                    .andExpect(jsonPath("$.results[0].professionalTitle").value(speaker2.getProfessionalTitle()));
        }
    }

    @Nested
    @DisplayName("method: getSpeaker(Long)")
    class GetSpeakerMethod {
        private Speaker speaker;

        @BeforeEach
        void setUp() {
            this.speaker = speakerRepository.save(SpeakerMock.newSpeakerDomain());
        }

        @Test
        @DisplayName("when called with unknown id, then it should return a 404 error")
        void whenCalledWithUnknownId_shouldReturn404Error() throws Exception {
            mockMvc.perform(get(ENDPOINT + "/speakers/{id}", 100L))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Speaker with ID 100 not found."))
                    .andExpect(jsonPath("$.details").isEmpty());

        }

        @Test
        @DisplayName("when called with exsting id, then it should return the matching speaker")
        void whenCalledWithExistingID_shouldReturnTheMatchingSpeaker() throws Exception {
            mockMvc.perform(get(ENDPOINT + "/speakers/{id}", speaker.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(speaker.getId()))
                    .andExpect(jsonPath("$.firstName").value(speaker.getFirstName()))
                    .andExpect(jsonPath("$.lastName").value(speaker.getLastName()))
                    .andExpect(jsonPath("$.email").value(speaker.getEmail()))
                    .andExpect(jsonPath("$.professionalTitle").value(speaker.getProfessionalTitle()));
        }
    }

    @Nested
    @DisplayName("method: createSpeaker(CreateSpeakerRequest)")
    class CreateSpeakerMethod {

        @Test
        @DisplayName("when called without firstName, then it should return a 400 error")
        void whenCalledWithoutFirstName_shouldReturn400Error() throws Exception {
            CreateSpeakerRequest createSpeaker = new CreateSpeakerRequest(
                    null,
                    "Adam",
                    "john@test.com",
                    "Software Engineer"
            );

            final MockHttpServletRequestBuilder request = post(ENDPOINT + "/speakers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("firstName"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called without lastName, then it should return a 400 error")
        void whenCalledWithoutLastName_shouldReturn400Error() throws Exception {
            CreateSpeakerRequest createSpeaker = new CreateSpeakerRequest(
                    "John",
                    null,
                    "john@test.com",
                    "Software Engineer"
            );

            final MockHttpServletRequestBuilder request = post(ENDPOINT + "/speakers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("lastName"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called without email, then it should return a 400 error")
        void whenCalledWithoutEmail_shouldReturn400Error() throws Exception {
            CreateSpeakerRequest createSpeaker = new CreateSpeakerRequest(
                    "John",
                    "Adam",
                    null,
                    "Software Engineer"
            );

            final MockHttpServletRequestBuilder request = post(ENDPOINT + "/speakers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("email"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called with invalid email, then it should return a 400 error")
        void whenCalledWithInvalidEmail_shouldReturn400Error() throws Exception {
            CreateSpeakerRequest createSpeaker = new CreateSpeakerRequest(
                    "John",
                    "Adam",
                    "invalid",
                    "Software Engineer"
            );

            final MockHttpServletRequestBuilder request = post(ENDPOINT + "/speakers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("email"))
                    .andExpect(jsonPath("$.details[0].message").value("the field must contain a valid email"));
        }

        @Test
        @DisplayName("when called without professionalTitle, then it should return a 400 error")
        void whenCalledWithoutProfessionalTitle_shouldReturn400Error() throws Exception {
            CreateSpeakerRequest createSpeaker = new CreateSpeakerRequest(
                    "John",
                    "Adam",
                    "john@test.com",
                    null
            );

            final MockHttpServletRequestBuilder request = post(ENDPOINT + "/speakers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("professionalTitle"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called with valid payload, then it should return 201 and persist the speaker")
        void whenCalledWithValidPayload_shouldReturn201AndPersistTheSpeaker() throws Exception {
            CreateSpeakerRequest createSpeaker = new CreateSpeakerRequest(
                    "John",
                    "Adam",
                    "john@test.com",
                    "Software Engineer"
            );

            final MockHttpServletRequestBuilder request = post(ENDPOINT + "/speakers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.lastName").value("Adam"))
                    .andExpect(jsonPath("$.email").value("john@test.com"))
                    .andExpect(jsonPath("$.professionalTitle").value("Software Engineer"));

            assertThat(speakerRepository.findByQuery(new SpeakerQuery(null, null, 10L, 1L)).items()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("method: updateSpeaker(Long, UpdateSpeakerRequest)")
    class UpdateSpeakerMethod {
        private Speaker speaker;

        @BeforeEach
        void setUp() {
            this.speaker = speakerRepository.save(SpeakerMock.newSpeakerDomain());
        }

        @Test
        @DisplayName("when called with unknown id, then it should return a 404 error")
        void whenCalledWithUnknownId_shouldReturn404Error() throws Exception {
            UpdateSpeakerRequest updateSpeaker = new UpdateSpeakerRequest(
                    null,
                    null,
                    null,
                    null
            );

            final MockHttpServletRequestBuilder request = patch(ENDPOINT + "/speakers/{id}", 100L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Speaker with ID 100 not found."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when called with invalid payload, then it should return 400 error")
        void whenCalledWithInvalidPayload_shouldReturn400Error() throws Exception {
            UpdateSpeakerRequest updateSpeaker = new UpdateSpeakerRequest(
                    UUID.randomUUID().toString().repeat(10),
                    null,
                    "invalid email",
                    null
            );

            final MockHttpServletRequestBuilder request = patch(ENDPOINT + "/speakers/{id}", speaker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("email"))
                    .andExpect(jsonPath("$.details[0].message").value("the field must contain a valid email"))
                    .andExpect(jsonPath("$.details[1].field").value("firstName"))
                    .andExpect(jsonPath("$.details[1].message").value("the field must contain at most 200 characters"));
        }

        @Test
        @DisplayName("when called with firstName, then it should update it")
        void whenCalledWithFirstName_shouldUpdateIt() throws Exception {
            UpdateSpeakerRequest updateSpeaker = new UpdateSpeakerRequest(
                    "John",
                    null,
                    null,
                    null
            );

            final MockHttpServletRequestBuilder request = patch(ENDPOINT + "/speakers/{id}", speaker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(speakerRepository.findById(speaker.getId()).orElseThrow().getFirstName()).isEqualTo("John");
        }

        @Test
        @DisplayName("when called with lastName, then it should update it")
        void whenCalledWithLastName_shouldUpdateIt() throws Exception {
            UpdateSpeakerRequest updateSpeaker = new UpdateSpeakerRequest(
                    null,
                    "Adam",
                    null,
                    null
            );

            final MockHttpServletRequestBuilder request = patch(ENDPOINT + "/speakers/{id}", speaker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(speakerRepository.findById(speaker.getId()).orElseThrow().getLastName()).isEqualTo("Adam");
        }

        @Test
        @DisplayName("when called with email, then it should update it")
        void whenCalledWithEmail_shouldUpdateIt() throws Exception {
            UpdateSpeakerRequest updateSpeaker = new UpdateSpeakerRequest(
                    null,
                    null,
                    "john@test.com",
                    null
            );

            final MockHttpServletRequestBuilder request = patch(ENDPOINT + "/speakers/{id}", speaker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(speakerRepository.findById(speaker.getId()).orElseThrow().getEmail()).isEqualTo("john@test.com");
        }

        @Test
        @DisplayName("when called with professionalTitle, then it should update it")
        void whenCalledWithProfessionalTitle_shouldUpdateIt() throws Exception {
            UpdateSpeakerRequest updateSpeaker = new UpdateSpeakerRequest(
                    null,
                    null,
                    null,
                    "Software Engineer"
            );

            final MockHttpServletRequestBuilder request = patch(ENDPOINT + "/speakers/{id}", speaker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSpeaker));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(speakerRepository.findById(speaker.getId()).orElseThrow().getProfessionalTitle()).isEqualTo("Software Engineer");
        }
    }

    @Nested
    @DisplayName("method: deleteSpeaker(Long)")
    class DeleteSpeakerMethod {
        private Speaker speaker;

        @BeforeEach
        void setUp() {
            this.speaker = speakerRepository.save(SpeakerMock.newSpeakerDomain());
        }

        @Test
        @DisplayName("when called with unknown id, then it should return a 404 error")
        void whenCalledWithUnknownId_shouldReturn404Error() throws Exception {
            mockMvc.perform(delete(ENDPOINT + "/speakers/{id}", 100L))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Speaker with ID 100 was not found."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when called with existing id, then it should return 200 and delete the matching speaker")
        void whenCalledWithExistingId_shouldReturn200AndDeleteTheMatchingSpeaker() throws Exception {
            mockMvc.perform(delete(ENDPOINT + "/speakers/{id}", speaker.getId()))
                    .andDo(print())
                    .andExpect(status().isOk());

            assertThat(speakerRepository.findById(speaker.getId())).isEmpty();
        }
    }
}