package com.raphael.conferenceapp.conferencemanagement.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.mock.ParticipantMock;
import com.raphael.conferenceapp.conferencemanagement.persistence.JpaParticipantRepository;
import com.raphael.conferenceapp.conferencemanagement.persistence.ParticipantEntity;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private JpaParticipantRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT = "/admin";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Nested
    @DisplayName("method: getParticipants(Long, SearchParticipantsRequest)")
    class GetParticipantsMethod {
        private Participant participant1;
        private Participant participant2;
        private Participant participant3;

        @BeforeEach
        void setUp() {
            this.participant1 = ParticipantMock.newParticipantDomain().toBuilder().conferenceId(1L).build();
            this.participant1 = repository.save(ParticipantEntity.fromDomain(participant1)).toDomain();

            this.participant2 = ParticipantMock.newParticipantDomain().toBuilder().conferenceId(2L).build();
            this.participant2 = repository.save(ParticipantEntity.fromDomain(participant2)).toDomain();

            this.participant3 = ParticipantMock.newParticipantDomain().toBuilder().conferenceId(1L).build();
            this.participant3 = repository.save(ParticipantEntity.fromDomain(participant3)).toDomain();
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
                    .andExpect(jsonPath("$.results[0].subscribedAt").value(participant1.getSubscribedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].conferenceId").value(participant1.getConferenceId()))
                    .andExpect(jsonPath("$.results[1].id").value(participant3.getId()))
                    .andExpect(jsonPath("$.results[1].name").value(participant3.getName()))
                    .andExpect(jsonPath("$.results[1].email").value(participant3.getEmail()))
                    .andExpect(jsonPath("$.results[1].subscribedAt").value(participant3.getSubscribedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
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
                    .andExpect(jsonPath("$.results[0].subscribedAt").value(participant1.getSubscribedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
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
                    .andExpect(jsonPath("$.results[0].subscribedAt").value(participant1.getSubscribedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
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
                    .andExpect(jsonPath("$.results[0].subscribedAt").value(participant3.getSubscribedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(jsonPath("$.results[0].conferenceId").value(participant3.getConferenceId()));
        }
    }
}