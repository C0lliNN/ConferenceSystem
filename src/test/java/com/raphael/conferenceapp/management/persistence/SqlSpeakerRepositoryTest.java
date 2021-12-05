package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.PaginatedItems;
import com.raphael.conferenceapp.management.entity.PaginationConstants;
import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.entity.SpeakerQuery;
import com.raphael.conferenceapp.management.mock.SpeakerMock;
import com.raphael.conferenceapp.utils.config.DatabaseTestAutoConfiguration;
import com.raphael.conferenceapp.utils.initializer.DatabaseContainerInitializer;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Transactional
@EnableAutoConfiguration
@SpringBootTest(
        classes = {
                DatabaseTestAutoConfiguration.class,
                SqlSpeakerRepository.class,
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ContextConfiguration(initializers = DatabaseContainerInitializer.class)
class SqlSpeakerRepositoryTest {

    @Autowired
    private SqlSpeakerRepository repository;

    @Nested
    @DisplayName("method: findByQuery(SpeakerQuery)")
    class FindByQueryMethod {
        private Speaker speaker1;
        private Speaker speaker2;
        private Speaker speaker3;

        @BeforeEach
        void setUp() {
            this.speaker1 = SpeakerMock.newSpeakerDomain()
                    .toBuilder()
                    .firstName("John")
                    .build();

            this.speaker2 = SpeakerMock.newSpeakerDomain()
                    .toBuilder()
                    .firstName("Mary")
                    .build();

            this.speaker3 = SpeakerMock.newSpeakerDomain()
                    .toBuilder()
                    .firstName("Alfred")
                    .build();

            speaker1 = repository.save(speaker1);
            speaker2 = repository.save(speaker2);
            speaker3 = repository.save(speaker3);
        }

        @Test
        @DisplayName("when called with empty query, then it should return all speakers")
        void whenCalledWithEmptyQuery_shouldReturnAllSpeakers() {
            SpeakerQuery query = new SpeakerQuery(null, null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);

            PaginatedItems<Speaker> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(3)
                    .containsExactly(speaker3, speaker1, speaker2);

            assertThat(paginatedItems.totalItems()).isEqualTo(3L);
            assertThat(paginatedItems.currentPage()).isOne();
            assertThat(paginatedItems.totalPages()).isOne();
            assertThat(paginatedItems.offset()).isZero();
            assertThat(paginatedItems.limit()).isEqualTo(PaginationConstants.DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("when query contains firstName, then it should return only the speakers matching it")
        void whenQueryContainsFirstName_shouldReturnOnlyTheSpeakersMatchingIt() {
            SpeakerQuery query = new SpeakerQuery("fred", null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);

            PaginatedItems<Speaker> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(1)
                    .containsExactly(speaker3);

            assertThat(paginatedItems.totalItems()).isEqualTo(1L);
            assertThat(paginatedItems.currentPage()).isOne();
            assertThat(paginatedItems.totalPages()).isOne();
            assertThat(paginatedItems.offset()).isZero();
            assertThat(paginatedItems.limit()).isEqualTo(PaginationConstants.DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("when query contains email, then it should return only the speakers matching it")
        void whenQueryContainsEmail_shouldReturnOnlyTheSpeakersMatchingIt() {
            SpeakerQuery query = new SpeakerQuery(null, speaker2.getEmail(), PaginationConstants.DEFAULT_PAGE_SIZE, 0L);

            PaginatedItems<Speaker> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(1)
                    .containsExactly(speaker2);

            assertThat(paginatedItems.totalItems()).isEqualTo(1L);
            assertThat(paginatedItems.currentPage()).isOne();
            assertThat(paginatedItems.totalPages()).isOne();
            assertThat(paginatedItems.offset()).isZero();
            assertThat(paginatedItems.limit()).isEqualTo(PaginationConstants.DEFAULT_PAGE_SIZE);
        }

        @Test
        @DisplayName("when query contains limit, then it should return only the amount of speakers matching it")
        void whenQueryContainsLimit_shouldReturnOnlyTheAmountSpeakersMatchingIt() {
            SpeakerQuery query = new SpeakerQuery(null, null, 2L, 0L);

            PaginatedItems<Speaker> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(2)
                    .containsExactly(speaker3, speaker1);

            assertThat(paginatedItems.totalItems()).isEqualTo(3L);
            assertThat(paginatedItems.currentPage()).isOne();
            assertThat(paginatedItems.totalPages()).isEqualTo(2L);
            assertThat(paginatedItems.offset()).isZero();
            assertThat(paginatedItems.limit()).isEqualTo(2L);
        }

        @Test
        @DisplayName("when query contains limit and offset, then it should return only the amount of speakers matching it")
        void whenQueryContainsLimitAndOffset_shouldReturnOnlyTheAmountOfSpeakersMatchingIt() {
            SpeakerQuery query = new SpeakerQuery(null, null, 2L, 2L);

            PaginatedItems<Speaker> paginatedItems = repository.findByQuery(query);

            assertThat(paginatedItems.items())
                    .hasSize(1)
                    .containsExactly(speaker2);

            assertThat(paginatedItems.totalItems()).isEqualTo(3L);
            assertThat(paginatedItems.currentPage()).isEqualTo(2L);
            assertThat(paginatedItems.totalPages()).isEqualTo(2L);
            assertThat(paginatedItems.offset()).isEqualTo(2L);
            assertThat(paginatedItems.limit()).isEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("method: findById(Long)")
    class FindByIdMethod {
        private Speaker speaker;

        @BeforeEach
        void setUp() {
            speaker = repository.save(SpeakerMock.newSpeakerDomain());
        }

        @Test
        @DisplayName("when called with unknown id, then it should return optional empty")
        void whenCalledWithUnknownId_shouldReturnOptionalEmpty() {
            assertThat(repository.findById(1000L)).isEmpty();
        }

        @Test
        @DisplayName("when called with existing id, then it should return the speaker matching it")
        void whenCalledWithExistingId_shouldReturnTheSpeakerMatchingIt() {
            assertThat(repository.findById(speaker.getId())).hasValue(speaker);
        }
    }

    @Nested
    @DisplayName("method: save(Speaker)")
    class SaveMethod {

        @Test
        @DisplayName("when called, then it should persist the speaker")
        void whenCalled_shouldPersistTheSpeaker() {
            Speaker speaker = repository.save(SpeakerMock.newSpeakerDomain());

            assertThat(repository.findById(speaker.getId())).hasValue(speaker);
        }
    }

    @Nested
    @DisplayName("method: delete(Long)")
    class DeleteMethod {
        private Speaker speaker;

        @BeforeEach
        void setUp() {
            this.speaker = repository.save(SpeakerMock.newSpeakerDomain());
        }

        @Test
        @DisplayName("when called with existing id, then it should delete the speaker")
        void whenCalledWithExistingId_shouldDeleteTheSpeaker() {
            repository.delete(speaker.getId());

            assertThat(repository.findById(speaker.getId())).isEmpty();
        }

        @Test
        @DisplayName("when called with unknown id, then it should throw an exception")
        void whenCalledWithUnknownId_shouldNotThrowAnException() {
            assertThatCode(() -> repository.delete(500L))
                    .isInstanceOf(EmptyResultDataAccessException.class);
        }
    }
}