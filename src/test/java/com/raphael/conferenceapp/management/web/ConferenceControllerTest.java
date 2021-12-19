package com.raphael.conferenceapp.management.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphael.conferenceapp.auth.usecase.TokenGenerator;
import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.entity.ConferenceQuery;
import com.raphael.conferenceapp.management.entity.PaginatedItems;
import com.raphael.conferenceapp.management.entity.Session;
import com.raphael.conferenceapp.management.mock.ConferenceMock;
import com.raphael.conferenceapp.management.mock.SessionMock;
import com.raphael.conferenceapp.management.persistence.SqlConferenceRepository;
import com.raphael.conferenceapp.management.persistence.SqlSessionRepository;
import com.raphael.conferenceapp.management.persistence.SqlSpeakerRepository;
import com.raphael.conferenceapp.management.usecase.request.CreateConferenceRequest;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class ConferenceControllerTest {
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
    private TokenGenerator tokenGenerator;

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
                    .andExpect(jsonPath("$.results[0].participantLimit").value(conference1.getParticipantLimit()))
                    .andExpect(jsonPath("$.results[0].userId").value(conference1.getUserId()))
                    .andExpect(jsonPath("$.results[1].id").value(conference2.getId()))
                    .andExpect(jsonPath("$.results[1].title").value(conference2.getTitle()))
                    .andExpect(jsonPath("$.results[1].description").value(conference2.getDescription()))
                    .andExpect(jsonPath("$.results[1].startTime").value(conference2.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[1].endTime").value(conference2.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[1].participantLimit").value(conference2.getParticipantLimit()))
                    .andExpect(jsonPath("$.results[1].userId").value(conference2.getUserId()))
                    .andExpect(jsonPath("$.results[2].id").value(conference3.getId()))
                    .andExpect(jsonPath("$.results[2].title").value(conference3.getTitle()))
                    .andExpect(jsonPath("$.results[2].description").value(conference3.getDescription()))
                    .andExpect(jsonPath("$.results[2].startTime").value(conference3.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[2].endTime").value(conference3.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME)))
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
                    .andExpect(jsonPath("$.results[0].userId").value(conference2.getUserId()));
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
                    .andExpect(jsonPath("$.userId").value(3L));

            PaginatedItems<Conference> paginatedConferences = conferenceRepository.findByQuery(new ConferenceQuery(null, null, null, null, 10L, 0L));

            assertThat(paginatedConferences.items()).hasSize(1);
        }
    }
}