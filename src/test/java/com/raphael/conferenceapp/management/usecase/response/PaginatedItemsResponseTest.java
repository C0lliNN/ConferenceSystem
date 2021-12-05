package com.raphael.conferenceapp.management.usecase.response;

import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.entity.PaginatedItems;
import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.mock.ConferenceMock;
import com.raphael.conferenceapp.management.mock.SpeakerMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PaginatedItemsResponseTest {

    @Nested
    @DisplayName("method: fromPaginatedConferences(PaginatedItems<Conference>)")
    class FromPaginatedConferencesMethod {

        @Test
        @DisplayName("when called, then it should create a response object")
        void whenCalled_shouldCreateAResponseObject() {
            Conference conference1 = ConferenceMock.newConferenceDomain();
            Conference conference2 = ConferenceMock.newConferenceDomain();

            PaginatedItems<Conference> paginatedItems = new PaginatedItems<>(
                    List.of(conference1, conference2),
                    10L,
                    2L,
                    0L
            );

            PaginatedItemsResponse<ConferenceResponse> expected = new PaginatedItemsResponse<>(
                    List.of(ConferenceResponse.fromDomain(conference1), ConferenceResponse.fromDomain(conference2)),
                    paginatedItems.currentPage(),
                    paginatedItems.limit(),
                    paginatedItems.totalItems(),
                    paginatedItems.totalPages()
            );

            PaginatedItemsResponse<ConferenceResponse> actual = PaginatedItemsResponse.fromPaginatedConferences(paginatedItems);

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: fromPaginatedSpeakers(PaginatedItems<Speaker>")
    class FromPaginatedSpeakersMethod {

        @Test
        @DisplayName("when called, then it should create a response object")
        void whenCalled_shouldCreateAResponseObject() {
            Speaker speaker1 = SpeakerMock.newSpeakerDomain();
            Speaker speaker2 = SpeakerMock.newSpeakerDomain();

            PaginatedItems<Speaker> paginatedItems = new PaginatedItems<>(
                    List.of(speaker1, speaker2),
                    10L,
                    2L,
                    0L
            );

            PaginatedItemsResponse<SpeakerResponse> expected = new PaginatedItemsResponse<>(
                    List.of(SpeakerResponse.fromDomain(speaker1), SpeakerResponse.fromDomain(speaker2)),
                    paginatedItems.currentPage(),
                    paginatedItems.limit(),
                    paginatedItems.totalItems(),
                    paginatedItems.totalPages()
            );

            PaginatedItemsResponse<SpeakerResponse> actual = PaginatedItemsResponse.fromPaginatedSpeakers(paginatedItems);

            assertThat(actual).isEqualTo(expected);
        }
    }
}