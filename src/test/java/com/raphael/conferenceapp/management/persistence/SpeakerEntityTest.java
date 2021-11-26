package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.mock.SpeakerMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpeakerEntityTest {

    @Nested
    @DisplayName("method: toDomain()")
    class ToDomainMethod {

        @Test
        @DisplayName("when called, then it should return the correct domain object")
        void whenCalled_shouldReturnTheCorrectDomainObject() {
            SpeakerEntity entity = SpeakerMock.newSpeakerEntity();

            Speaker expected = Speaker.builder()
                    .id(entity.getId())
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .email(entity.getEmail())
                    .professionalTitle(entity.getProfessionalTitle())
                    .build();

            Speaker actual = entity.toDomain();

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: fromDomain(Speaker)")
    class FromDomainMethod {

        @Test
        @DisplayName("when called with null, then it should return null")
        void whenCalledWithNull_shouldReturnNull() {
            assertThat(SpeakerEntity.fromDomain(null)).isNull();
        }

        @Test
        @DisplayName("when called with non-null parameter, then it should return the correct entity object")
        void whenCalledWithNonNullParameter_shouldReturnTheCorrectEntityObject() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();

            SpeakerEntity expected = new SpeakerEntity(
                    speaker.getId(),
                    speaker.getFirstName(),
                    speaker.getLastName(),
                    speaker.getEmail(),
                    speaker.getProfessionalTitle()
            );

            SpeakerEntity actual = SpeakerEntity.fromDomain(speaker);

            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
            assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
            assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
            assertThat(actual.getProfessionalTitle()).isEqualTo(expected.getProfessionalTitle());
        }
    }

}