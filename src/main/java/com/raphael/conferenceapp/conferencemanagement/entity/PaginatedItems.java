package com.raphael.conferenceapp.conferencemanagement.entity;

import java.util.Collection;


public record PaginatedItems<T>(
        Collection<T> items,
        Long limit,
        Long totalItems,
        Long offset) {

    public Long totalPages() {
        return (long) Math.ceil((double) totalItems / (double) limit);
    }

    public Long currentPage()  {
        return (offset / limit) + 1;
    }
}
