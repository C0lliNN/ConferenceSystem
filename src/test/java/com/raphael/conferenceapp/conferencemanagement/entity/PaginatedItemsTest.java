package com.raphael.conferenceapp.conferencemanagement.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PaginatedItemsTest {

    @Nested
    @DisplayName("method: totalPages()")
    class TotalPagesMethod {

        @ParameterizedTest
        @CsvSource(value = {
                "10,5,1",
                "10,10,1",
                "10,15,2",
                "25,50,2",
                "25,100,4",
                "25,101,5",
        })
        @DisplayName("when called, then it should return the correct result")
        void whenCalled_shouldReturnTheCorrectResult(Long limit, Long totalItems, Long expected) {
            PaginatedItems<String> paginatedItems = new PaginatedItems<>(null, limit, totalItems, null);

            assertThat(paginatedItems.totalPages()).isEqualTo(expected);
        }

    }

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
        @DisplayName("when called, then it should return the correct result")
        void whenCalled_shouldReturnTheCorrectResult(Long limit, Long offset, Long expected) {
            PaginatedItems<String> paginatedItems = new PaginatedItems<>(null, limit, null, offset);

            assertThat(paginatedItems.currentPage()).isEqualTo(expected);
        }
    }
}