package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.PaginationConstants;
import com.raphael.conferenceapp.conferencemanagement.entity.ParticipantQuery;

public record SearchParticipantsRequest(
        String name,
        String email,
        Long conferenceId,
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

    public SearchParticipantsRequest withConferenceId(Long conferenceId) {
        return new SearchParticipantsRequest(
                name,
                email,
                conferenceId,
                perPage,
                page
        );
    }

    public ParticipantQuery toQuery() {
        return new ParticipantQuery(
                name,
                email,
                conferenceId,
                perPage(),
                offset()
        );
    }
}
