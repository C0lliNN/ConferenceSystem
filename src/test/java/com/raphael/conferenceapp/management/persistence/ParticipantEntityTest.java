package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Participant;
import com.raphael.conferenceapp.management.mock.ParticipantMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParticipantEntityTest {

    @Nested
    @DisplayName("method: toDomain()")
    class ToDomainMethod {

        @Test
        @DisplayName("when called, then it should return the correct domain object")
        void whenCalled_shouldReturnTheCorrectDomainObject() {
            ParticipantEntity participantEntity = ParticipantMock.newParticipantEntity();

            Participant expected = Participant.builder()
                    .id(participantEntity.getId())
                    .name(participantEntity.getName())
                    .email(participantEntity.getEmail())
                    .subscribedAt(participantEntity.getSubscribedAt())
                    .conferenceId(participantEntity.getConferenceId())
                    .build();

            Participant actual = participantEntity.toDomain();

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: fromDomain(Participant)")
    class FromDomainMethod {

        @Test
        @DisplayName("when called, then it should create the correct entity object")
        void whenCalled_shouldCreateTheCorrectEntityObject() {
            Participant participant = ParticipantMock.newParticipantDomain();

            ParticipantEntity actual = ParticipantEntity.fromDomain(participant);

            assertThat(actual.getId()).isEqualTo(participant.getId());
            assertThat(actual.getName()).isEqualTo(participant.getName());
            assertThat(actual.getEmail()).isEqualTo(participant.getEmail());
            assertThat(actual.getSubscribedAt()).isEqualTo(participant.getSubscribedAt());
            assertThat(actual.getConferenceId()).isEqualTo(participant.getConferenceId());
        }
    }
}