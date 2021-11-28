package com.raphael.conferenceapp.management.usecase;

import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.exception.DeletionConflictException;
import com.raphael.conferenceapp.management.exception.EntityNotFoundException;
import com.raphael.conferenceapp.management.usecase.request.CreateConferenceRequest;
import com.raphael.conferenceapp.management.usecase.request.SearchConferencesRequest;
import com.raphael.conferenceapp.management.usecase.request.UpdateConferenceRequest;
import com.raphael.conferenceapp.management.usecase.response.ConferenceResponse;
import com.raphael.conferenceapp.management.usecase.response.PaginatedItemsResponse;
import lombok.AllArgsConstructor;

import javax.annotation.ManagedBean;

@ManagedBean
@AllArgsConstructor
public class ConferenceUseCase {
    private ConferenceRepository conferenceRepository;

    public PaginatedItemsResponse<ConferenceResponse> getConferences(SearchConferencesRequest request) {
        return PaginatedItemsResponse.fromDomain(conferenceRepository.findByQuery(request.toQuery()));
    }

    public ConferenceResponse getConference(Long conferenceId) {
        return conferenceRepository.findById(conferenceId)
                .map(ConferenceResponse::fromDomain)
                .orElseThrow(() -> new EntityNotFoundException("Conference with ID %d not found.", conferenceId));
    }

    public ConferenceResponse createConference(CreateConferenceRequest request, Long userId) {
        return ConferenceResponse.fromDomain(conferenceRepository.save(request.toDomain(userId)));
    }

    public void updateConference(Long conferenceId, UpdateConferenceRequest request) {
        Conference existingConference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference with ID %d not found.", conferenceId));

        Conference updatedConference = request.apply(existingConference);

        conferenceRepository.save(updatedConference);
    }

    public void deleteConference(Long conferenceId) {
        Conference existingConference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference with ID %d not found.", conferenceId));

        if (existingConference.hasSessions()) {
            throw new DeletionConflictException("Could not delete Conference %d because it has associated sessions", conferenceId);
        }

        conferenceRepository.delete(conferenceId);
    }
}
