package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Session;
import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.mock.SessionMock;
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
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@EnableAutoConfiguration
@SpringBootTest(
        classes = {
                DatabaseTestAutoConfiguration.class,
                SqlSpeakerRepository.class,
                SqlSessionRepository.class,
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ContextConfiguration(initializers = DatabaseContainerInitializer.class)
class SqlSessionRepositoryTest {

    @Autowired
    private SqlSpeakerRepository speakerRepository;

    @Autowired
    private SqlSessionRepository sessionRepository;

    @Nested
    @DisplayName("method: findByConferenceId(Long)")
    class FindByConferenceIdMethod {
        private Long conferenceId = 3L;

        private Session session1;
        private Session session2;
        private Session session3;

        @BeforeEach
        void setUp() {
            this.session1 = SessionMock.newSessionDomain()
                    .toBuilder()
                    .conferenceId(conferenceId)
                    .build();

            Speaker speaker1 = speakerRepository.save(session1.getSpeaker());

            this.session1 = session1.toBuilder().speaker(speaker1).build();
            this.session1 = sessionRepository.save(session1);

            this.session2 = SessionMock.newSessionDomain()
                    .toBuilder()
                    .conferenceId(conferenceId)
                    .build();

            Speaker speaker2 = speakerRepository.save(session2.getSpeaker());

            this.session2 = session2.toBuilder().speaker(speaker2).build();
            this.session2 = sessionRepository.save(this.session2);

            this.session3 = SessionMock.newSessionDomain();

            Speaker speaker3 = speakerRepository.save(session2.getSpeaker());

            this.session3 = session3.toBuilder().speaker(speaker3).build();
            this.session3 = sessionRepository.save(session3);
        }

        @Test
        @DisplayName("when called, then it should return all persisted sessions matching the conferenceId")
        void whenCalled_shouldReturnAllPersistedSessionsMatchingTheConferenceId() {
            Collection<Session> sessions = sessionRepository.findByConferenceId(conferenceId);

            assertThat(sessions)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(session1, session2);
        }
    }

    @Nested
    @DisplayName("method: save(Session)")
    class SessionMethod {
        private Session session;

        @BeforeEach
        void setUp() {
            this.session = SessionMock.newSessionDomain();

            Speaker speaker3 = speakerRepository.save(session.getSpeaker());

            this.session = session.toBuilder().speaker(speaker3).build();
        }

        @Test
        @DisplayName("when called, it should persist the session")
        void whenCalled_shouldPersistTheSession() {
            this.session = sessionRepository.save(session);

            assertThat(sessionRepository.findByConferenceId(session.getConferenceId())).containsOnly(session);
        }
    }

    @Nested
    @DisplayName("method: delete(Long)")
    class DeleteMethod {
        private Session session;

        @BeforeEach
        void setUp() {
            this.session = SessionMock.newSessionDomain();

            Speaker speaker3 = speakerRepository.save(session.getSpeaker());

            this.session = session.toBuilder().speaker(speaker3).build();
            this.session = sessionRepository.save(session);
        }

        @Test
        @DisplayName("when called with unknown id, then it should throw an exception")
        void whenCalledWithUnknownId_shouldThrowAnException() {
            assertThatThrownBy(() -> sessionRepository.delete(500L))
                    .isInstanceOf(EmptyResultDataAccessException.class);
        }

        @Test
        @DisplayName("when called with existing id, then it should delete the session")
        void whenCalledWithExistingId_shouldDeleteTheSession() {
            sessionRepository.delete(session.getId());

            assertThat(sessionRepository.findByConferenceId(session.getConferenceId())).isEmpty();
        }
    }
}