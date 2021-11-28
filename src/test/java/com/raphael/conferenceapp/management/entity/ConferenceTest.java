package com.raphael.conferenceapp.management.entity;

import com.raphael.conferenceapp.management.mock.SessionMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConferenceTest {

    @Nested
    @DisplayName("method: getSessions()")
    class GetSessionsMethod {

        @Test
        @DisplayName("when sessions is null, then it should return an empty list")
        void whenSessionsIsNull_shouldReturnAnEmptyList() {
            Conference conference = new Conference(null, null, null, null, null, null, null, null);

            assertThat(conference.getSessions()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("when sessions is not null, then it should return it")
        void whenSessionsIsNotNull_shouldReturnIt() {
            Session session = SessionMock.newSessionDomain();
            Conference conference = new Conference(null, null, null, null, null, null, null, List.of(session));

            assertThat(conference.getSessions()).isEqualTo(List.of(session));
        }
    }

    @Nested
    @DisplayName("method: hasSessions()")
    class HasSessionMethod {

        @Test
        @DisplayName("when session is null, then it should return false")
        void whenSessionIsNull_shouldReturnFalse() {
            Conference conference = new Conference(null, null, null, null, null, null, null, null);

            assertThat(conference.hasSessions()).isFalse();
        }

        @Test
        @DisplayName("when session is empty, then it should return false")
        void whenSessionIsEmpty_shouldReturnFalse() {
            Conference conference = new Conference(null, null, null, null, null, null, null, Collections.emptyList());

            assertThat(conference.hasSessions()).isFalse();
        }

        @Test
        @DisplayName("when session is not empty, then it should return true")
        void whenSessionIsNotEmpty_shouldReturnTrue() {
            Conference conference = new Conference(null, null, null, null, null, null, null, List.of(SessionMock.newSessionDomain()));

            assertThat(conference.hasSessions()).isTrue();
        }
    }
}