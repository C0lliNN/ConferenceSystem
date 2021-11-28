package com.raphael.conferenceapp.management.usecase.response;

import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.mock.ConferenceMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConferenceResponseTest {

    @Nested
    @DisplayName("method: fromDomain(Conference)")
    class FromDomainMethod {

        @Test
        @DisplayName("when called, then it should create a response object")
        void whenCalled_shouldCreateAResponseObject() {
            Conference conference = ConferenceMock.newConferenceDomain();

            ConferenceResponse expected = new ConferenceResponse(
                    conference.getId(),
                    conference.getTitle(),
                    conference.getDescription(),
                    conference.getStartTime(),
                    conference.getEndTime(),
                    conference.getParticipantLimit(),
                    conference.getUserId(),
                    conference.getSessions().stream().map(SessionResponse::fromDomain).toList()
            );

            ConferenceResponse actual = ConferenceResponse.fromDomain(conference);

            assertThat(actual).isEqualTo(expected);
        }
    }
}