package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.Session;
import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;
import com.raphael.conferenceapp.conferencemanagement.mock.SessionMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateSessionRequestTest {

    @Nested
    @DisplayName("method: apply(Session)")
    class ApplyMethod {

        @Test
        @DisplayName("when all fields are null, then it should return the same object")
        void whenAllFieldsAreNull_shouldReturnTheSameObject() {
            Session session = SessionMock.newSessionDomain();
            UpdateSessionRequest request = new UpdateSessionRequest(null, null, null, null, null, null);

            assertThat(request.apply(session)).isEqualTo(session);
        }

        @Test
        @DisplayName("when the title is present, then it should modify it")
        void whenTheTitleIsPresent_shouldModifyIt() {
            Session session = SessionMock.newSessionDomain();
            UpdateSessionRequest request = new UpdateSessionRequest("new title", null, null, null, null, null);

            Session expected = session.toBuilder().title("new title").build();
            Session actual = request.apply(session);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the description is present, then it should modify it")
        void whenTheDescriptionIsPresent_shouldModifyIt() {
            Session session = SessionMock.newSessionDomain();
            UpdateSessionRequest request = new UpdateSessionRequest(null, "new description", null, null, null, null);

            Session expected = session.toBuilder().description("new description").build();
            Session actual = request.apply(session);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the startTime is present, then it should modify it")
        void whenTheStartTimeIsPresent_shouldModifyIt() {
            LocalDateTime newStartTime = LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 55);
            Session session = SessionMock.newSessionDomain();
            UpdateSessionRequest request = new UpdateSessionRequest(null, null, newStartTime, null, null, null);

            Session expected = session.toBuilder().startTime(newStartTime).build();
            Session actual = request.apply(session);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the endTime is present, then it should modify it")
        void whenTheEndTimeIsPresent_shouldModifyIt() {
            LocalDateTime newEndTime = LocalDateTime.of(2021, Month.NOVEMBER, 20, 15, 55);
            Session session = SessionMock.newSessionDomain();
            UpdateSessionRequest request = new UpdateSessionRequest(null, null, null, newEndTime, null, null);

            Session expected = session.toBuilder().endTime(newEndTime).build();
            Session actual = request.apply(session);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the accessLink is present, then it should modify it")
        void whenTheAccessLinkIsPresent_shouldModifyIt() {
            Session session = SessionMock.newSessionDomain();
            UpdateSessionRequest request = new UpdateSessionRequest(null, null, null, null, "new link", null);

            Session expected = session.toBuilder().accessLink("new link").build();
            Session actual = request.apply(session);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the speakerId is present, then it should modify it")
        void whenTheSpeakerIdIsPresent_shouldModifyIt() {
            Session session = SessionMock.newSessionDomain();
            UpdateSessionRequest request = new UpdateSessionRequest(null, null, null, null, null, 5L);

            Session expected = session.toBuilder().speaker(Speaker.builder().id(5L).build()).build();
            Session actual = request.apply(session);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when multiple fields are present, then it should modify them all")
        void whenMultipleFieldsArePresent_shouldModifyThemAll() {
            Session session = SessionMock.newSessionDomain();
            UpdateSessionRequest request = new UpdateSessionRequest("new title", "new description", null, null, null, null);

            Session expected = session.toBuilder().title("new title").description("new description").build();
            Session actual = request.apply(session);

            assertThat(actual).isEqualTo(expected);
        }
    }
}