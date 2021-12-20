package com.raphael.conferenceapp.conferencemanagement.usecase.response;

import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.mock.ParticipantMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParticipantResponseTest {

    @Nested
    @DisplayName("method: fromDomain(Participant)")
    class FromDomainMethod {

        @Test
        @DisplayName("when called, then it should create the correct response object")
        void whenCalled_shouldCreateTheCorrectResponseObject() {
            Participant participant = ParticipantMock.newParticipantDomain();

            ParticipantResponse expected = new ParticipantResponse(
                    participant.getId(),
                    participant.getName(),
                    participant.getEmail(),
                    participant.getSubscribedAt(),
                    participant.getConferenceId()
            );

            ParticipantResponse actual = ParticipantResponse.fromDomain(participant);

            assertThat(actual).isEqualTo(expected);
        }
    }
}