package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.ConferenceQuery;
import com.raphael.conferenceapp.management.entity.PaginationConstants;

import java.time.LocalDateTime;

public record SearchConferencesRequest(
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long userId,
        Long perPage,
        Long page) {

    public Long page() {
        if (page == null || page == 0) {
            return 1L;
        }

        return page;
    }

    public Long perPage() {
        if (perPage == null || perPage == 0) {
            return PaginationConstants.DEFAULT_PAGE_SIZE;
        }

        return perPage;
    }

    public Long offset() {
        return (page() - 1) * perPage();
    }

    public ConferenceQuery toQuery() {
        return new ConferenceQuery(
                title,
                startTime,
                endTime,
                userId,
                perPage(),
                offset()
        );
    }
}
