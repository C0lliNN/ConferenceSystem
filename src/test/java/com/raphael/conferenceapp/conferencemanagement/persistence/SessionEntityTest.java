package com.raphael.conferenceapp.conferencemanagement.persistence;

import com.raphael.conferenceapp.conferencemanagement.entity.Session;
import com.raphael.conferenceapp.conferencemanagement.mock.SessionMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SessionEntityTest {

    @Nested
    @DisplayName("method: toDomain()")
    class ToDomainMethod {

        @Test
        @DisplayName("when speaker is null, then it should create a session without the speaker")
        void whenSpeakerIsNull_shouldCreateSessionWithoutTheSpeaker() {
            SessionEntity entity = SessionMock.newSessionEntity();
            entity.setSpeaker(null);

            Session expected = Session.builder()
                    .id(entity.getId())
                    .startTime(entity.getStartTime())
                    .endTime(entity.getEndTime())
                    .title(entity.getTitle())
                    .description(entity.getDescription())
                    .accessLink(entity.getAccessLink())
                    .conferenceId(entity.getConferenceId())
                    .speaker(null)
                    .build();

            Session actual = entity.toDomain();

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when speaker is not null, then it should return the correct domain object")
        void whenSpeakerIsNotNull_shouldReturnTheCorrectDomainObject() {
            SessionEntity entity = SessionMock.newSessionEntity();

            Session expected = Session.builder()
                    .id(entity.getId())
                    .startTime(entity.getStartTime())
                    .endTime(entity.getEndTime())
                    .title(entity.getTitle())
                    .description(entity.getDescription())
                    .accessLink(entity.getAccessLink())
                    .conferenceId(entity.getConferenceId())
                    .speaker(entity.getSpeaker().toDomain())
                    .build();

            Session actual = entity.toDomain();

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: fromDomain(Session)")
    class FromDomainMethod {

        @Test
        @DisplayName("when called with null, then it should return null")
        void whenCalledWithNull_shouldReturnNull() {
            assertThat(SessionEntity.fromDomain(null)).isNull();
        }

        @Test
        @DisplayName("when speaker is null, then should return a session without speaker")
        void whenSpeakerIsNull_shouldReturnASessionWithoutSpeaker() {
            Session session = SessionMock.newSessionDomain().toBuilder().speaker(null).build();

            SessionEntity expected = new SessionEntity(
                    session.getId(),
                    session.getStartTime(),
                    session.getEndTime(),
                    session.getTitle(),
                    session.getDescription(),
                    session.getAccessLink(),
                    session.getConferenceId(),
                    null
            );

            SessionEntity actual = SessionEntity.fromDomain(session);

            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getStartTime()).isEqualTo(expected.getStartTime());
            assertThat(actual.getEndTime()).isEqualTo(expected.getEndTime());
            assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
            assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
            assertThat(actual.getAccessLink()).isEqualTo(expected.getAccessLink());
            assertThat(actual.getSpeaker()).isNull();
        }

        @Test
        @DisplayName("when speaker is not null, then it should return the correct entity object")
        void whenSpeakerIsNotNull_shouldReturnTheCorrectEntityObject() {
            Session session = SessionMock.newSessionDomain();

            SessionEntity expected = new SessionEntity(
                    session.getId(),
                    session.getStartTime(),
                    session.getEndTime(),
                    session.getTitle(),
                    session.getDescription(),
                    session.getAccessLink(),
                    session.getConferenceId(),
                    SpeakerEntity.fromDomain(session.getSpeaker())
            );

            SessionEntity actual = SessionEntity.fromDomain(session);

            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getStartTime()).isEqualTo(expected.getStartTime());
            assertThat(actual.getEndTime()).isEqualTo(expected.getEndTime());
            assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
            assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
            assertThat(actual.getAccessLink()).isEqualTo(expected.getAccessLink());

            assertThat(actual.getSpeaker().getId()).isEqualTo(expected.getSpeaker().getId());
            assertThat(actual.getSpeaker().getFirstName()).isEqualTo(expected.getSpeaker().getFirstName());
            assertThat(actual.getSpeaker().getLastName()).isEqualTo(expected.getSpeaker().getLastName());
            assertThat(actual.getSpeaker().getEmail()).isEqualTo(expected.getSpeaker().getEmail());
            assertThat(actual.getSpeaker().getProfessionalTitle()).isEqualTo(expected.getSpeaker().getProfessionalTitle());
        }
    }

}