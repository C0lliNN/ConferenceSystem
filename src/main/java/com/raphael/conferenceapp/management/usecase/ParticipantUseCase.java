package com.raphael.conferenceapp.management.usecase;

import com.raphael.conferenceapp.management.usecase.request.SearchParticipantsRequest;
import com.raphael.conferenceapp.management.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.management.usecase.response.ParticipantResponse;
import lombok.AllArgsConstructor;

import javax.annotation.ManagedBean;

@ManagedBean
@AllArgsConstructor
public class ParticipantUseCase {
    private final ParticipantRepository repository;

    public PaginatedItemsResponse<ParticipantResponse> getParticipants(SearchParticipantsRequest request) {
        return PaginatedItemsResponse.fromPaginatedParticipants(repository.findByQuery(request.toQuery()));
    }
}
