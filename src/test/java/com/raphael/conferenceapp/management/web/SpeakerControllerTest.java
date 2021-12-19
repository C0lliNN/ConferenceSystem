package com.raphael.conferenceapp.management.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.entity.SpeakerQuery;
import com.raphael.conferenceapp.management.mock.SpeakerMock;
import com.raphael.conferenceapp.management.persistence.SqlSpeakerRepository;
import com.raphael.conferenceapp.management.usecase.request.CreateSpeakerRequest;
import com.raphael.conferenceapp.management.usecase.request.UpdateSpeakerRequest;
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
class SpeakerControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private SqlSpeakerRepository speakerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT = "/admin";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
                    .firstName("Annie")
                    .build();
            this.speaker1 = speakerRepository.save(speaker1);

            this.speaker2 = SpeakerMock.newSpeakerDomain()
                    .toBuilder()
                    .firstName("Bob")
                    .build();
            this.speaker2 = speakerRepository.save(speaker2);

            this.speaker3 = SpeakerMock.newSpeakerDomain()
                    .toBuilder()
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