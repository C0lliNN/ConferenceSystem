package com.raphael.conferenceapp.conferencemanagement.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ConferenceQueryTest {

    @Nested
    @DisplayName("method: currentPage()")
    class CurrentPageMethod {

        @ParameterizedTest
        @CsvSource(value = {
                "10,0,1",
                "10,25,3",
                "10,40,5",
                "15,15,2",
                "15,45,4"
        })
        @DisplayName("when called, then it should return the correct current page")
        void whenCalled_shouldReturnTheCorrectCurrentPage(Long limit, Long offset, Long expected) {
            ConferenceQuery query = new ConferenceQuery(null, null, null, null, limit, offset);

            assertThat(query.currentPage()).isEqualTo(expected);
        }
    }

}