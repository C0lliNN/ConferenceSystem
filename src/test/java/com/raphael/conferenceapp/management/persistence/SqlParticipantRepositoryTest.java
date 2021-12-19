package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.PaginatedItems;
import com.raphael.conferenceapp.management.entity.PaginationConstants;
import com.raphael.conferenceapp.management.entity.Participant;
import com.raphael.conferenceapp.management.entity.ParticipantQuery;
import com.raphael.conferenceapp.management.mock.ParticipantMock;
import com.raphael.conferenceapp.utils.config.DatabaseTestAutoConfiguration;
import com.raphael.conferenceapp.utils.initializer.DatabaseContainerInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@EnableAutoConfiguration
@SpringBootTest(
        classes = {
                DatabaseTestAutoConfiguration.class,
                SqlParticipantRepository.class,
                JpaParticipantRepository.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ContextConfiguration(initializers = DatabaseContainerInitializer.class)
class SqlParticipantRepositoryTest {

    @Autowired
    private SqlParticipantRepository participantRepository;

    @Autowired
    private JpaParticipantRepository jpaRepository;

    @Nested
    @DisplayName("method: findByQuery(ParticipantQuery)")
    class ParticipantQueryMethod {
        private Participant participant1;
        private Participant participant2;
        private Participant participant3;

        @BeforeEach
        void setUp() {
            this.participant1 = ParticipantMock.newParticipantDomain();
            this.participant1 = jpaRepository.save(ParticipantEntity.fromDomain(participant1)).toDomain();

            this.participant2 = ParticipantMock.newParticipantDomain();
            this.participant2 = jpaRepository.save(ParticipantEntity.fromDomain(participant2)).toDomain();

            this.participant3 = ParticipantMock.newParticipantDomain();
            this.participant3 = jpaRepository.save(ParticipantEntity.fromDomain(participant3)).toDomain();
        }

        @Test
        @DisplayName("when called with empty query, then it should return all participants")
        void whenCalledWithEmptyQuery_shouldReturnAllParticipants() {
            ParticipantQuery query = new ParticipantQuery(null, null, null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);
            PaginatedItems<Participant> paginatedParticipants = participantRepository.findByQuery(query);

            assertThat(paginatedParticipants.currentPage()).isEqualTo(1L);
            assertThat(paginatedParticipants.totalItems()).isEqualTo(3L);
            assertThat(paginatedParticipants.totalPages()).isEqualTo(1L);
            assertThat(paginatedParticipants.limit()).isEqualTo(10L);
            assertThat(paginatedParticipants.items()).containsExactly(participant1, participant2, participant3);
        }

        @Test
        @DisplayName("when name is present, then it should only return the participants matching it")
        void whenNameIsPresent_shouldOnlyReturnTheParticipantsMatchingIt() {
            ParticipantQuery query = new ParticipantQuery(participant2.getName(), null, null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);
            PaginatedItems<Participant> paginatedParticipants = participantRepository.findByQuery(query);

            assertThat(paginatedParticipants.currentPage()).isEqualTo(1L);
            assertThat(paginatedParticipants.totalItems()).isEqualTo(1L);
            assertThat(paginatedParticipants.totalPages()).isEqualTo(1L);
            assertThat(paginatedParticipants.limit()).isEqualTo(10L);
            assertThat(paginatedParticipants.items()).containsExactly(participant2);
        }

        @Test
        @DisplayName("when email is present, then it should only return the participants matching it")
        void whenEmailIsPresent_shouldOnlyReturnTheParticipantsMatchingIt() {
            ParticipantQuery query = new ParticipantQuery(null, participant2.getEmail(), null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);
            PaginatedItems<Participant> paginatedParticipants = participantRepository.findByQuery(query);

            assertThat(paginatedParticipants.currentPage()).isEqualTo(1L);
            assertThat(paginatedParticipants.totalItems()).isEqualTo(1L);
            assertThat(paginatedParticipants.totalPages()).isEqualTo(1L);
            assertThat(paginatedParticipants.limit()).isEqualTo(10L);
            assertThat(paginatedParticipants.items()).containsExactly(participant2);
        }

        @Test
        @DisplayName("when limit and offset are present, then it should only return the participants matching it")
        void whenLimitAndOffsetArePresent_shouldOnlyReturnTheParticipantsMatchingIt() {
            ParticipantQuery query = new ParticipantQuery(null, null, null, 1L, 1L);
            PaginatedItems<Participant> paginatedParticipants = participantRepository.findByQuery(query);

            assertThat(paginatedParticipants.currentPage()).isEqualTo(2L);
            assertThat(paginatedParticipants.totalItems()).isEqualTo(3L);
            assertThat(paginatedParticipants.totalPages()).isEqualTo(3L);
            assertThat(paginatedParticipants.limit()).isEqualTo(1L);
            assertThat(paginatedParticipants.items()).containsExactly(participant2);
        }
    }
}