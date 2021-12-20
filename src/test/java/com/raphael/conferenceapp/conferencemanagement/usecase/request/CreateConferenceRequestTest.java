package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.mock.ConferenceMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateConferenceRequestTest {

    @Nested
    @DisplayName("method: toDomain(Long)")
    class ToDomainMethod {

        @Test
        @DisplayName("when called, then it should return the correct domain object")
        void whenCalled_shouldReturnTheCorrectDomainObject() {
            CreateConferenceRequest request = ConferenceMock.newCreateRequest();
            Long userId = 150L;

            Conference expected = Conference.builder()
                    .title(request.title())
                    .description(request.description())
                    .startTime(request.startTime())
                    .endTime(request.endTime())
                    .participantLimit(request.participantLimit())
                    .totalParticipants(0)
                    .userId(userId)
                    .build();

            Conference actual = request.toDomain(userId);

            assertThat(actual).isEqualTo(expected);

        }
    }
}