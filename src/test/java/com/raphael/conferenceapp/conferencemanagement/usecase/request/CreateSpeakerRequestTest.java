package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;
import com.raphael.conferenceapp.conferencemanagement.mock.SpeakerMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateSpeakerRequestTest {

    @Nested
    @DisplayName("method: toDomain()")
    class ToDomainMethod {

        @Test
        @DisplayName("when called, then it should create the correct domain object")
        void whenCalled_shouldCreateTheCorrectDomainObject() {
            CreateSpeakerRequest request = SpeakerMock.newCreateRequest();

            Speaker expected = Speaker.builder()
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .professionalTitle(request.professionalTitle())
                    .build();

            Speaker actual = request.toDomain();

            assertThat(actual).isEqualTo(expected);
        }
    }
}