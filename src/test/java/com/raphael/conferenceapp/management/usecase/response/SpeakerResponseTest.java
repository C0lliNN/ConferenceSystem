package com.raphael.conferenceapp.management.usecase.response;

import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.mock.SpeakerMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpeakerResponseTest {

    @Nested
    @DisplayName("method: fromDomain(Speaker)")
    class FromDomainMethod {

        @Test
        @DisplayName("when called, then it should create a response object")
        void whenCalled_shouldCreateAResponseObject() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();

            SpeakerResponse expected = new SpeakerResponse(
                    speaker.getId(),
                    speaker.getFirstName(),
                    speaker.getLastName(),
                    speaker.getEmail(),
                    speaker.getProfessionalTitle()
            );

            SpeakerResponse actual = SpeakerResponse.fromDomain(speaker);

            assertThat(actual).isEqualTo(expected);
        }
    }

}