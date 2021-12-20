package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.mock.ParticipantMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class SubscribeRequestTest {

    @Nested
    @DisplayName("method: toParticipant(Instant)")
    class ToParticipantMethod {

        @Test
        @DisplayName("when called, then it should return the correct domain object")
        void whenCalled_shouldReturnTheCorrectDomainObject() {
            Instant instant = Instant.now();
            SubscribeRequest request = ParticipantMock.newSubscribeRequest();

            Participant expected = Participant
                    .builder()
                    .name(request.name())
                    .email(request.email())
                    .conferenceId(request.conferenceId())
                    .subscribedAt(instant)
                    .build();

            Participant actual = request.toParticipant(instant);

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: withConferenceId(Long)")
    class WithConferenceIdMethod {

        @Test
        @DisplayName("when called, then it should change only the conferenceId")
        void whenCalled_shouldChangeOnlyTheConferenceId() {
            SubscribeRequest request = ParticipantMock.newSubscribeRequest();

            SubscribeRequest expected = new SubscribeRequest(request.name(), request.email(), 12L);
            SubscribeRequest actual = request.withConferenceId(12L);

            assertThat(actual).isEqualTo(expected);
        }
    }
}