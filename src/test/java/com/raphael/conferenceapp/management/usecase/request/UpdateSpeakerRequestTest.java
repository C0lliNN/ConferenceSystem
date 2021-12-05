package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.mock.SpeakerMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateSpeakerRequestTest {

    @Nested
    @DisplayName("method: apply(Speaker)")
    class ApplyMethod {

        @Test
        @DisplayName("when all fields are null, then it should return the same object")
        void whenAllFieldsAreNull_shouldReturnTheSameObject() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();
            UpdateSpeakerRequest request = new UpdateSpeakerRequest(null, null, null, null);

            Speaker actual = request.apply(speaker);

            assertThat(actual).isEqualTo(speaker);
        }

        @Test
        @DisplayName("when firstName is present, then it should modify it")
        void whenFirstNameIsPresent_shouldModifyIt() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();
            UpdateSpeakerRequest request = new UpdateSpeakerRequest("new name", null, null, null);

            Speaker expected = speaker.toBuilder().firstName("new name").build();
            Speaker actual = request.apply(speaker);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when lastName is present, then it should modify it")
        void whenLastNameIsPresent_shouldModifyIt() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();
            UpdateSpeakerRequest request = new UpdateSpeakerRequest(null, "new name", null, null);

            Speaker expected = speaker.toBuilder().lastName("new name").build();
            Speaker actual = request.apply(speaker);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when email is present, then it should modify it")
        void whenEmailIsPresent_shouldModifyIt() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();
            UpdateSpeakerRequest request = new UpdateSpeakerRequest(null, null, "new_email@test.com", null);

            Speaker expected = speaker.toBuilder().email("new_email@test.com").build();
            Speaker actual = request.apply(speaker);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when professionalTitle is present, then it should modify it")
        void whenProfessionalTitleIsPresent_shouldModifyIt() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();
            UpdateSpeakerRequest request = new UpdateSpeakerRequest(null, null, null, "new title");

            Speaker expected = speaker.toBuilder().professionalTitle("new title").build();
            Speaker actual = request.apply(speaker);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when multiple fields are present, then it should update them all")
        void whenMultipleFieldsArePresent_shouldUpdateThemAll() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();
            UpdateSpeakerRequest request = new UpdateSpeakerRequest("new name", null, null, "new title");

            Speaker expected = speaker.toBuilder().firstName("new name").professionalTitle("new title").build();
            Speaker actual = request.apply(speaker);

            assertThat(actual).isEqualTo(expected);
        }
    }
}