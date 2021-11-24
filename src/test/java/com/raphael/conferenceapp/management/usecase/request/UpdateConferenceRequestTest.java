package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.mock.ConferenceMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateConferenceRequestTest {

    @Nested
    @DisplayName("method: apply(Conference)")
    class ConferenceMethod {

        @Test
        @DisplayName("when all fields are null, then it should return the same object")
        void whenAllFieldsAreNull_shouldReturnTheSameObject() {
            Conference conference = ConferenceMock.newConferenceDomain();
            UpdateConferenceRequest request = new UpdateConferenceRequest(null, null, null, null, null);

            Conference expected = conference.toBuilder().build();
            Conference actual = request.apply(conference);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the title is present, then it should modify it")
        void whenTheTitleIsPresent_shouldModifyIt() {
            Conference conference = ConferenceMock.newConferenceDomain();
            UpdateConferenceRequest request = new UpdateConferenceRequest("newTitle", null, null, null, null);

            Conference expected = conference.toBuilder().title("newTitle").build();
            Conference actual = request.apply(conference);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the description is present, then it should modify it")
        void whenTheDescriptionIsPresent_shouldModifyIt() {
            Conference conference = ConferenceMock.newConferenceDomain();
            UpdateConferenceRequest request = new UpdateConferenceRequest(null, "newDescription", null, null, null);

            Conference expected = conference.toBuilder().description("newDescription").build();
            Conference actual = request.apply(conference);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the startTime is present, then it should modify it")
        void whenTheStartTimeIsPresent_shouldModifyIt() {
            Conference conference = ConferenceMock.newConferenceDomain();

            LocalDateTime startTime = LocalDateTime.of(2021, Month.NOVEMBER, 20, 14, 50);
            UpdateConferenceRequest request = new UpdateConferenceRequest(null, null, startTime, null, null);

            Conference expected = conference.toBuilder().startTime(startTime).build();
            Conference actual = request.apply(conference);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the endTime is present, then it should modify it")
        void whenTheEndTimeIsPresent_shouldModifyIt() {
            Conference conference = ConferenceMock.newConferenceDomain();

            LocalDateTime endTime = LocalDateTime.of(2021, Month.NOVEMBER, 20, 14, 50);
            UpdateConferenceRequest request = new UpdateConferenceRequest(null, null, null, endTime, null);

            Conference expected = conference.toBuilder().endTime(endTime).build();
            Conference actual = request.apply(conference);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when the participantLimit is present, then it should modify it")
        void whenTheParticipantLimitIsPresent_shouldModifyIt() {
            Conference conference = ConferenceMock.newConferenceDomain();
            UpdateConferenceRequest request = new UpdateConferenceRequest(null, null, null, null, 3);

            Conference expected = conference.toBuilder().participantLimit(3).build();
            Conference actual = request.apply(conference);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when multiple fields are present, then it should modify them")
        void whenMultipleFieldsArePresent_shouldModifyThem() {
            Conference conference = ConferenceMock.newConferenceDomain();
            UpdateConferenceRequest request = new UpdateConferenceRequest("newTitle", null, null, null, 3);

            Conference expected = conference.toBuilder().title("newTitle").participantLimit(3).build();
            Conference actual = request.apply(conference);

            assertThat(actual).isEqualTo(expected);
        }
    }

}