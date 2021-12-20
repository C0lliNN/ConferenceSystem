package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.PaginationConstants;
import com.raphael.conferenceapp.conferencemanagement.entity.SpeakerQuery;

public record SearchSpeakersRequest(
        String name,
        String email,
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

    public SpeakerQuery toQuery() {
        return new SpeakerQuery(name, email, perPage(), offset());
    }
}
