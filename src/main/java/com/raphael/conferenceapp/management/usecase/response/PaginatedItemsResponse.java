package com.raphael.conferenceapp.management.usecase.response;

import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.entity.PaginatedItems;

import java.util.Collection;

public record PaginatedItemsResponse<T>(
        Collection<T> results,
        Long currentPage,
        Long perPage,
        Long totalItems,
        Long totalPages) {

    public static PaginatedItemsResponse<ConferenceResponse> fromDomain(PaginatedItems<Conference> paginatedItems) {
        Collection<ConferenceResponse> conferences = paginatedItems.items()
                .stream()
                .map(ConferenceResponse::fromDomain)
                .toList();

        return new PaginatedItemsResponse<>(
                conferences,
                paginatedItems.currentPage(),
                paginatedItems.limit(),
                paginatedItems.totalItems(),
                paginatedItems.totalPages()
        );
    }
}
