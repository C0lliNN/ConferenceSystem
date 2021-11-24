package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.mock.ConferenceMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ConferenceEntityTest {

    @Nested
    @DisplayName("method: toDomain()")
    class ToDomainMethod {

        @Test
        @DisplayName("when called, then it should return the correct domain object")
        void whenCalled_shouldReturnTheCorrectDomainObject() {
            ConferenceEntity entity = ConferenceMock.newConferenceEntity();

            Conference expected = Conference.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .description(entity.getDescription())
                    .startTime(entity.getStartTime())
                    .endTime(entity.getEndTime())
                    .participantLimit(entity.getParticipantLimit())
                    .userId(entity.getUserId())
                    .sessions(Collections.emptyList())
                    .build();

            Conference actual = entity.toDomain();

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: fromDomain(Conference)")
    class FromDomainMethod {

        @Test
        @DisplayName("when called, then it should return the correct entity object")
        void whenCalled_shouldReturnTheCorrectEntityObject() {
            Conference conference = ConferenceMock.newConferenceDomain();

            ConferenceEntity expected = new ConferenceEntity(
                    conference.getId(),
                    conference.getTitle(),
                    conference.getDescription(),
                    conference.getStartTime(),
                    conference.getEndTime(),
                    conference.getParticipantLimit(),
                    conference.getUserId(),
                    Collections.emptyList()
            );

            ConferenceEntity actual = ConferenceEntity.fromDomain(conference);

            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
            assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
            assertThat(actual.getStartTime()).isEqualTo(expected.getStartTime());
            assertThat(actual.getEndTime()).isEqualTo(expected.getEndTime());
            assertThat(actual.getParticipantLimit()).isEqualTo(expected.getParticipantLimit());
            assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
            assertThat(actual.getSessions()).isEmpty();
        }
    }
}