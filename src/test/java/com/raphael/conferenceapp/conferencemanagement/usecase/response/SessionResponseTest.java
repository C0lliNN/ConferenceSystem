package com.raphael.conferenceapp.conferencemanagement.usecase.response;

import com.raphael.conferenceapp.conferencemanagement.entity.Session;
import com.raphael.conferenceapp.conferencemanagement.mock.SessionMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SessionResponseTest {

    @Nested
    @DisplayName("method: fromDomain(Session)")
    class FromDomainMethod {

        @Test
        @DisplayName("when called, then it should create a response object")
        void whenCalled_shouldCreateAResponseObject() {
            Session session = SessionMock.newSessionDomain();

            SessionResponse expected = new SessionResponse(
                    session.getId(),
                    session.getStartTime(),
                    session.getEndTime(),
                    session.getTitle(),
                    session.getDescription(),
                    session.getAccessLink(),
                    SpeakerResponse.fromDomain(session.getSpeaker())
            );

            SessionResponse actual = SessionResponse.fromDomain(session);

            assertThat(actual).isEqualTo(expected);
        }
    }
}