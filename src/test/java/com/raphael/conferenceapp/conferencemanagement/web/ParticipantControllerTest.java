package com.raphael.conferenceapp.conferencemanagement.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.mock.ConferenceMock;
import com.raphael.conferenceapp.conferencemanagement.persistence.JpaParticipantRepository;
import com.raphael.conferenceapp.conferencemanagement.persistence.ParticipantEntity;
import com.raphael.conferenceapp.conferencemanagement.persistence.SqlConferenceRepository;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SubscribeRequest;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

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
class ParticipantControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private SqlConferenceRepository conferenceRepository;

    @Autowired
    private JpaParticipantRepository jpaParticipantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT = "/participant";

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
    @DisplayName("method: getConference(Long)")
    class GetConferenceMethod {
        private Conference conference;

        @BeforeEach
        void setUp() {
            this.conference = conferenceRepository.save(ConferenceMock.newConferenceDomain());
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
    @DisplayName("method: subscribe(Long, SubscribeRequest)")
    class SubscribeMethod {

        @Test
        @DisplayName("when conference is not found, then it should return a 404 error")
        void whenConferenceIsNotFound_shouldReturn404Error() throws Exception {
            SubscribeRequest subscribeRequest = new SubscribeRequest("Raphael collin", "raphael@test.com", null);

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences/{id}", 20L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(subscribeRequest));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Conference with ID 20 was not found."))
                    .andExpect(jsonPath("$.details").isEmpty());

        }

        @Test
        @DisplayName("when name is empty, then it should return a 400 error")
        void whenNameIsEmpty_shouldReturn400Error() throws Exception {
            Conference conference = conferenceRepository.save(ConferenceMock.newConferenceDomain());
            SubscribeRequest subscribeRequest = new SubscribeRequest(null, "raphael@test.com", null);

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences/{id}", conference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(subscribeRequest));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("name"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when email is empty, then it should return a 400 error")
        void whenEmailIsEmpty_shouldReturn400Error() throws Exception {
            Conference conference = conferenceRepository.save(ConferenceMock.newConferenceDomain());
            SubscribeRequest subscribeRequest = new SubscribeRequest("Raphael Collin", "", null);

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences/{id}", conference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(subscribeRequest));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("email"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when email is invalid, then it should return a 400 error")
        void whenEmailIsInvalid_shouldReturn400Error() throws Exception {
            Conference conference = conferenceRepository.save(ConferenceMock.newConferenceDomain());
            SubscribeRequest subscribeRequest = new SubscribeRequest("Raphael Collin", "something", null);

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences/{id}", conference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(subscribeRequest));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("email"))
                    .andExpect(jsonPath("$.details[0].message").value("the field must contain a valid email"));
        }

        @Test
        @DisplayName("when conference is already full, then it should return a 409 error")
        void whenConferenceIsAlreadyFull_shouldReturn409Error() throws Exception {
            Conference conference = conferenceRepository.save(ConferenceMock
                    .newConferenceDomain()
                    .toBuilder()
                    .participantLimit(10)
                    .totalParticipants(10)
                    .build()
            );

            SubscribeRequest subscribeRequest = new SubscribeRequest("Raphael Collin", "raphael@test.com", null);

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences/{id}", conference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(subscribeRequest));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("The conference is already full."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when the participant is already registered for the given conference, then it should return a 409 error")
        void whenTheParticipantIsAlreadyRegisteredForTheGivenConference_shouldReturn404Error() throws Exception {
            Conference conference = conferenceRepository.save(ConferenceMock
                    .newConferenceDomain()
                    .toBuilder()
                    .participantLimit(10)
                    .totalParticipants(5)
                    .build()
            );

            SubscribeRequest subscribeRequest = new SubscribeRequest("Raphael Collin", "raphael@test.com", null);

            Participant participant = subscribeRequest.withConferenceId(conference.getId()).toParticipant(Instant.now());
            jpaParticipantRepository.save(ParticipantEntity.fromDomain(participant));

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences/{id}", conference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(subscribeRequest));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("The provided email is already registered in this conference"))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when all preconditions are met, then it should subscribe successfully")
        void whenAllPreconditionsAreMet_shouldSubscribeSuccessfully() throws Exception {
            Conference conference = conferenceRepository.save(ConferenceMock
                    .newConferenceDomain()
                    .toBuilder()
                    .participantLimit(10)
                    .totalParticipants(5)
                    .build()
            );

            SubscribeRequest subscribeRequest = new SubscribeRequest("Raphael Collin", "raphael@test.com", null);

            MockHttpServletRequestBuilder request = post(ENDPOINT + "/conferences/{id}", conference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(subscribeRequest));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Raphael Collin"))
                    .andExpect(jsonPath("$.conferenceId").value(conference.getId()))
                    .andExpect(jsonPath("$.email").value("raphael@test.com"));

            ParticipantEntity entity = jpaParticipantRepository.findAll().get(0);
            assertThat(entity.getName()).isEqualTo("Raphael Collin");
            assertThat(entity.getEmail()).isEqualTo("raphael@test.com");
        }
    }
}