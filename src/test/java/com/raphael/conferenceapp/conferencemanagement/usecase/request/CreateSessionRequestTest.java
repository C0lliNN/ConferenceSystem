package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.Session;
import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;
import com.raphael.conferenceapp.conferencemanagement.mock.SessionMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateSessionRequestTest {

    @Nested
    @DisplayName("method: withConferenceId(Long)")
    class WithConferenceIdMethod {

        @Test
        @DisplayName("when called, then it should return the correct object")
        void whenCalled_shouldReturnTheCorrectObject() {
            CreateSessionRequest request = SessionMock.newCreateRequest();

            CreateSessionRequest expected = new CreateSessionRequest(
                    request.title(),
                    request.description(),
                    request.startTime(),
                    request.endTime(),
                    request.accessLink(),
                    request.speakerId(),
                    5L
            );

            CreateSessionRequest actual = request.withConferenceId(5L);

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: toDomain()")
    class ToDomainMethod {

        @Test
        @DisplayName("when called, then it should create the correct session object")
        void whenCalled_shouldCreateTheCorrectSessionObject() {
            CreateSessionRequest request = SessionMock.newCreateRequest();

            Session expected = Session.builder()
                    .title(request.title())
                    .description(request.description())
                    .startTime(request.startTime())
                    .endTime(request.endTime())
                    .accessLink(request.accessLink())
                    .speaker(Speaker.builder().id(request.speakerId()).build())
                    .conferenceId(request.conferenceId())
                    .build();

            Session actual = request.toDomain();

            assertThat(actual).isEqualTo(expected);
        }
    }
}