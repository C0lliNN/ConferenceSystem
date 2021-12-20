package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.PaginationConstants;
import com.raphael.conferenceapp.conferencemanagement.entity.ParticipantQuery;
import com.raphael.conferenceapp.conferencemanagement.mock.ParticipantMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SearchParticipantsRequestTest {

    @Nested
    @DisplayName("method: page()")
    class PageMethod {

        @Test
        @DisplayName("when page is null, then it should return 1")
        void whenPageIsNull_shouldReturn1() {
            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, null, null, null);

            assertThat(request.page()).isOne();
        }

        @Test
        @DisplayName("when page is 0, then it should return 1")
        void whenPageIsZero_shouldReturn1() {
            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, null, null, 0L);

            assertThat(request.page()).isOne();
        }

        @Test
        @DisplayName("when page is greater than 0, then it should return it")
        void whenPageIsGreaterThanZero_shouldReturnIt() {
            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null,  null, null, 5L);

            assertThat(request.page()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("method: perPage()")
    class PerPageMethod {

        @Test
        @DisplayName("when perPage is null, then it should return 10")
        void whenPerPageIsNull_shouldReturn10() {
            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, null, null, null);

            assertThat(request.perPage()).isEqualTo(10);
        }

        @Test
        @DisplayName("when perPage is 0, then it should return 10")
        void whenPerPageIsZero_shouldReturn1() {
            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, null, 0L, null);

            assertThat(request.perPage()).isEqualTo(10);
        }

        @Test
        @DisplayName("when perPage is greater than 0, then it should return it")
        void whenPerPageIsGreaterThanZero_shouldReturnIt() {
            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, null, 5L, null);

            assertThat(request.perPage()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("method: offset()")
    class OffsetMethod {

        @ParameterizedTest
        @CsvSource({
                "0,0,0",
                "10,1,0",
                "10,2,10",
                "10,4,30",
                "15,2,15",
        })
        @DisplayName("when called, then it should return the correct offset")
        void whenCalled_shouldReturnTheCorrectOffset(Long perPage, Long page, Long expected) {
            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, null, perPage, page);

            assertThat(request.offset()).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: toQuery()")
    class ToQueryMethod {

        @Test
        @DisplayName("when called and query is empty, then it should return the correct domain object")
        void whenCalledAndQueryIsEmpty_shouldReturnTheCorrectDomainObject() {
            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, null, null, null);

            ParticipantQuery expected = new ParticipantQuery(null, null, null, PaginationConstants.DEFAULT_PAGE_SIZE, 0L);
            ParticipantQuery actual = request.toQuery();

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("when called, then it should return the correct domain object")
        void whenCalled_shouldReturnTheCorrectDomainObject() {
            SearchParticipantsRequest request = ParticipantMock.newSearchRequest();

            ParticipantQuery expected = new ParticipantQuery(
                    request.name(),
                    request.email(),
                    request.conferenceId(),
                    request.perPage(),
                    request.offset()

            );
            ParticipantQuery actual = request.toQuery();

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: withConferenceId(Long)")
    class WithConferenceIdMethod {

        @Test
        @DisplayName("when called, then it should update the conferenceId")
        void whenCalled_shouldUpdateTheConferenceId() {
            SearchParticipantsRequest request = ParticipantMock.newSearchRequest();
            Long conferenceId = 10L;

            SearchParticipantsRequest actual = request.withConferenceId(conferenceId);

            assertThat(actual.conferenceId()).isEqualTo(conferenceId);
            assertThat(actual.name()).isEqualTo(request.name());
            assertThat(actual.email()).isEqualTo(request.email());

        }
    }
}