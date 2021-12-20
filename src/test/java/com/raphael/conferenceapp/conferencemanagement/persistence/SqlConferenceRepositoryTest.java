package com.raphael.conferenceapp.conferencemanagement.persistence;

import com.raphael.conferenceapp.utils.config.DatabaseTestAutoConfiguration;
import com.raphael.conferenceapp.utils.initializer.DatabaseContainerInitializer;
import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.entity.ConferenceQuery;
import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.entity.PaginationConstants;
import com.raphael.conferenceapp.conferencemanagement.mock.ConferenceMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Transactional
@EnableAutoConfiguration
@SpringBootTest(
        classes = {
                DatabaseTestAutoConfiguration.class,
                SqlConferenceRepository.class,
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ContextConfiguration(initializers = DatabaseContainerInitializer.class)
class SqlConferenceRepositoryTest {

    @Autowired
    private SqlConferenceRepository repository;

    @Nested
    @DisplayName("method: findByQuery(ConferenceQuery)")
    class FindByQueryMethod {
        private Conference conference1;
        private Conference conference2;
        private Conference conference3;
        private Conference conference4;
        private Conference conference5;

        @BeforeEach
        void setUp() {
            conference1 = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .id(null)
                    .title("title1")
                    .startTime(LocalDateTime.of(2021, Month.NOVEMBER, 13, 10, 0))
                    .endTime(LocalDateTime.of(2021, Month.NOVEMBER, 23, 10, 0))
                    .userId(1L)
                    .build();

            conference2 = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .id(null)
                    .startTime(LocalDateTime.of(2021, Month.NOVEMBER, 20, 8, 30))
                    .endTime(LocalDateTime.of(2021, Month.NOVEMBER, 30, 8, 30))
                    .userId(2L)
                    .build();

            conference3 = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .id(null)
                    .startTime(LocalDateTime.of(2021, Month.DECEMBER, 5, 10, 0))
                    .endTime(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 0))
                    .userId(2L)
                    .build();

            conference4 = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .id(null)
                    .startTime(LocalDateTime.of(2021, Month.DECEMBER, 6, 10, 0))
                    .endTime(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 0))
                    .userId(3L)
                    .build();

            conference5 = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .id(null)
                    .startTime(LocalDateTime.of(2021, Month.DECEMBER, 13, 10, 0))
                    .endTime(LocalDateTime.of(2021, Month.DECEMBER, 23, 10, 0))
                    .userId(4L)
                    .build();

            conference1 = repository.save(conference1);
            conference2 = repository.save(conference2);
            conference3 = repository.save(conference3);
            conference4 = repository.save(conference4);
            conference5 = repository.save(conference5);
        }

        @Test
        @DisplayName("when called with empty query, then it should return all conferences")
        void whenCalledWithEmptyQuery_shouldReturnAllConferences() {
            ConferenceQuery query = new ConferenceQuery(null, null, null, null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);

            PaginatedItems<Conference> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(5)
                    .containsExactly(conference5, conference4, conference3, conference2, conference1);

            assertThat(paginatedItems.currentPage()).isEqualTo(1L);
            assertThat(paginatedItems.totalPages()).isEqualTo(1L);
            assertThat(paginatedItems.totalItems()).isEqualTo(5L);
            assertThat(paginatedItems.limit()).isEqualTo(10L);
            assertThat(paginatedItems.offset()).isEqualTo(0L);
        }

        @Test
        @DisplayName("when called with query containing title, then it should return only the matching conferences")
        void whenCalledQueryContainingTitle_shouldReturnOnlyTheMatchingConferences() {
            ConferenceQuery query = new ConferenceQuery("title", null, null, null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);

            PaginatedItems<Conference> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(1)
                    .containsOnly(conference1);

            assertThat(paginatedItems.currentPage()).isEqualTo(1L);
            assertThat(paginatedItems.totalPages()).isEqualTo(1L);
            assertThat(paginatedItems.totalItems()).isEqualTo(1L);
            assertThat(paginatedItems.limit()).isEqualTo(10L);
            assertThat(paginatedItems.offset()).isEqualTo(0L);
        }

        @Test
        @DisplayName("when called with query containing startTime, then it should return only the matching conferences")
        void whenCalledQueryContainingStartTime_shouldReturnOnlyTheMatchingConferences() {
            LocalDateTime startTime = LocalDateTime.of(2021, Month.DECEMBER, 5, 8, 0);
            ConferenceQuery query = new ConferenceQuery(null, startTime, null, null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);

            PaginatedItems<Conference> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(3)
                    .containsOnly(conference3, conference4, conference5);

            assertThat(paginatedItems.currentPage()).isEqualTo(1L);
            assertThat(paginatedItems.totalPages()).isEqualTo(1L);
            assertThat(paginatedItems.totalItems()).isEqualTo(3L);
            assertThat(paginatedItems.limit()).isEqualTo(10L);
            assertThat(paginatedItems.offset()).isEqualTo(0L);
        }

        @Test
        @DisplayName("when called with query containing endTime, then it should return only the matching conferences")
        void whenCalledQueryContainingEndTime_shouldReturnOnlyTheMatchingConferences() {
            LocalDateTime endTime = LocalDateTime.of(2021, Month.NOVEMBER, 30, 23, 30);
            ConferenceQuery query = new ConferenceQuery(null, null, endTime, null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);

            PaginatedItems<Conference> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(2)
                    .containsOnly(conference2, conference1);

            assertThat(paginatedItems.currentPage()).isEqualTo(1L);
            assertThat(paginatedItems.totalPages()).isEqualTo(1L);
            assertThat(paginatedItems.totalItems()).isEqualTo(2L);
            assertThat(paginatedItems.limit()).isEqualTo(10L);
            assertThat(paginatedItems.offset()).isEqualTo(0L);
        }

        @Test
        @DisplayName("when called with query containing userId, then it should return only the matching conferences")
        void whenCalledQueryContainingUserId_shouldReturnOnlyTheMatchingConferences() {
            ConferenceQuery query = new ConferenceQuery(null, null, null, 2L, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);

            PaginatedItems<Conference> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(2)
                    .containsOnly(conference2, conference3);

            assertThat(paginatedItems.currentPage()).isEqualTo(1L);
            assertThat(paginatedItems.totalPages()).isEqualTo(1L);
            assertThat(paginatedItems.totalItems()).isEqualTo(2L);
            assertThat(paginatedItems.limit()).isEqualTo(10L);
            assertThat(paginatedItems.offset()).isEqualTo(0L);
        }

        @Test
        @DisplayName("when called with query containing multiple filters, then it should return only the matching conferences")
        void whenCalledQueryContainingMultipleFilters_shouldReturnOnlyTheMatchingConferences() {
            LocalDateTime startTime = LocalDateTime.of(2021, Month.NOVEMBER, 15, 15, 30);
            LocalDateTime endTime = LocalDateTime.of(2021, Month.NOVEMBER, 30, 23, 30);
            ConferenceQuery query = new ConferenceQuery(null, startTime, endTime, null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);

            PaginatedItems<Conference> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(1)
                    .containsOnly(conference2);

            assertThat(paginatedItems.currentPage()).isEqualTo(1L);
            assertThat(paginatedItems.totalPages()).isEqualTo(1L);
            assertThat(paginatedItems.totalItems()).isEqualTo(1L);
            assertThat(paginatedItems.limit()).isEqualTo(10L);
            assertThat(paginatedItems.offset()).isEqualTo(0L);
        }

        @Test
        @DisplayName("when called with limit, then it should return only the matching conferences")
        void whenCalledWithLimit_shouldReturnOnlyTheMatchingConferences() {
            ConferenceQuery query = new ConferenceQuery(null, null, null, null, 2L, 0L);

            PaginatedItems<Conference> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(2)
                    .containsOnly(conference5, conference4);

            assertThat(paginatedItems.currentPage()).isEqualTo(1L);
            assertThat(paginatedItems.totalPages()).isEqualTo(3L);
            assertThat(paginatedItems.totalItems()).isEqualTo(5L);
            assertThat(paginatedItems.limit()).isEqualTo(2L);
            assertThat(paginatedItems.offset()).isEqualTo(0L);
        }

        @Test
        @DisplayName("when called with offset, then it should return only the matching conferences")
        void whenCalledWithOffset_shouldReturnOnlyTheMatchingConferences() {
            ConferenceQuery query = new ConferenceQuery(null, null, null, null, 2L, 2L);

            PaginatedItems<Conference> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(2)
                    .containsOnly(conference3, conference2);

            assertThat(paginatedItems.currentPage()).isEqualTo(2L);
            assertThat(paginatedItems.totalPages()).isEqualTo(3L);
            assertThat(paginatedItems.totalItems()).isEqualTo(5L);
            assertThat(paginatedItems.limit()).isEqualTo(2L);
            assertThat(paginatedItems.offset()).isEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("method: findById(Long)")
    class FindByIdMethod {
        private Conference conference;

        @BeforeEach
        void setUp() {
            conference = ConferenceMock.newConferenceDomain();
            conference = repository.save(conference);
        }

        @Test
        @DisplayName("when called with unknown id, then it should return Optional empty")
        void whenCalledWithUnknownId_shouldReturnOptionalEmpty() {
            assertThat(repository.findById(100L)).isEmpty();
        }

        @Test
        @DisplayName("when called with existing id, then it should return the matching conference wrapped in an Optional")
        void whenCalledWithExistingId_shouldReturnTheMatchingConferenceWrappedInAnOptional() {
            assertThat(repository.findById(conference.getId())).hasValue(conference);
        }
    }

    @Nested
    @DisplayName("method: save(Conference)")
    class SaveMethod {

        @Test
        @DisplayName("when called, then it should persist the conference")
        void whenCalled_shouldPersistTheConference() {
            Conference conference = ConferenceMock.newConferenceDomain();

            Conference expectedConference = repository.save(conference);

            assertThat(repository.findById(expectedConference.getId())).hasValue(expectedConference);
        }
    }

    @Nested
    @DisplayName("method: delete(Long)")
    class DeleteMethod {
        private Conference conference;

        @BeforeEach
        void setUp() {
            conference = ConferenceMock.newConferenceDomain();
            conference = repository.save(conference);
        }

        @Test
        @DisplayName("when called with existing id, then it should delete the conference")
        void whenCalledWithExistingId_shouldDeleteTheConference() {
            repository.delete(conference.getId());

            assertThat(repository.findById(conference.getId())).isEmpty();
        }

        @Test
        @DisplayName("when called with unknown id, then it should throw an exception")
        void whenCalledWithUnknownId_shouldNotThrowAnException() {
            assertThatCode(() -> repository.delete(500L))
                    .isInstanceOf(EmptyResultDataAccessException.class);
        }
    }
}