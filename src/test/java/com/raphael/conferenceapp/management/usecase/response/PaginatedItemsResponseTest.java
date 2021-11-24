package com.raphael.conferenceapp.management.usecase.response;

import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.entity.PaginatedItems;
import com.raphael.conferenceapp.management.mock.ConferenceMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PaginatedItemsResponseTest {

    @Nested
    @DisplayName("method: fromDomain(PaginatedItems<Conference>)")
    class FromDomainMethod {

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

            PaginatedItemsResponse<ConferenceResponse> actual = PaginatedItemsResponse.fromDomain(paginatedItems);

            assertThat(actual).isEqualTo(expected);
        }
    }
}