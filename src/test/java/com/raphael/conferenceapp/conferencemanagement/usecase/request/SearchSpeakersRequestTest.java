package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.SpeakerQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SearchSpeakersRequestTest {

    @Nested
    @DisplayName("method: page()")
    class PageMethod {

        @Test
        @DisplayName("when page is null, then it should return 1")
        void whenPageIsNull_shouldReturn1() {
            SearchConferencesRequest request = new SearchConferencesRequest(null, null, null, null, null, null);

            assertThat(request.page()).isOne();
        }

        @Test
        @DisplayName("when page is 0, then it should return 1")
        void whenPageIsZero_shouldReturn1() {
            SearchConferencesRequest request = new SearchConferencesRequest(null, null, null, null, null, 0L);

            assertThat(request.page()).isOne();
        }

        @Test
        @DisplayName("when page is greater than 0, then it should return it")
        void whenPageIsGreaterThanZero_shouldReturnIt() {
            SearchConferencesRequest request = new SearchConferencesRequest(null, null, null, null, null, 5L);

            assertThat(request.page()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("method: perPage()")
    class PerPageMethod {

        @Test
        @DisplayName("when perPage is null, then it should return 10")
        void whenPerPageIsNull_shouldReturn10() {
            SearchConferencesRequest request = new SearchConferencesRequest(null, null, null, null, null, null);

            assertThat(request.perPage()).isEqualTo(10);
        }

        @Test
        @DisplayName("when perPage is 0, then it should return 10")
        void whenPerPageIsZero_shouldReturn1() {
            SearchConferencesRequest request = new SearchConferencesRequest(null, null, null, null, 0L, null);

            assertThat(request.perPage()).isEqualTo(10);
        }

        @Test
        @DisplayName("when perPage is greater than 0, then it should return it")
        void whenPerPageIsGreaterThanZero_shouldReturnIt() {
            SearchConferencesRequest request = new SearchConferencesRequest(null, null, null, null, 5L, null);

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
            SearchConferencesRequest request = new SearchConferencesRequest(null, null, null, null, perPage, page);

            assertThat(request.offset()).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: toQuery()")
    class ToQueryMethod {

        @Test
        @DisplayName("when called and request is empty, then it should return the correct domain object")
        void whenCalledAndRequestIsEmpty_shouldReturnTheCorrectDomainObject() {
            SearchSpeakersRequest request = new SearchSpeakersRequest(null, null, null, null);

            SpeakerQuery expected = new SpeakerQuery(request.name(), request.email(), request.perPage(), request.offset());
            SpeakerQuery actual = request.toQuery();

            assertThat(actual).isEqualTo(expected);
        }
    }
}