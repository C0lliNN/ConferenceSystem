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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ManagedBean;

@ManagedBean
@AllArgsConstructor
public class ConferenceUseCase {
    private ConferenceRepository conferenceRepository;

    public PaginatedItemsResponse<ConferenceResponse> getConferences(SearchConferencesRequest request) {
        return PaginatedItemsResponse.fromPaginatedConferences(conferenceRepository.findByQuery(request.toQuery()));
    }

    public ConferenceResponse getConference(Long conferenceId) {
        return conferenceRepository.findById(conferenceId)
                .map(ConferenceResponse::fromDomain)
                .orElseThrow(() -> new EntityNotFoundException("Conference with ID %d was not found.", conferenceId));
    }

    public ConferenceResponse createConference(CreateConferenceRequest request, Long userId) {
        return ConferenceResponse.fromDomain(conferenceRepository.save(request.toDomain(userId)));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateConference(Long conferenceId, UpdateConferenceRequest request) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference with ID %d was not found.", conferenceId));

        conferenceRepository.save(request.apply(conference));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteConference(Long conferenceId) {
        Conference existingConference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference with ID %d was not found.", conferenceId));

        if (existingConference.hasSessions()) {
            throw new DeletionConflictException("Could not delete Conference %d because it has associated sessions", conferenceId);
        }

        conferenceRepository.delete(conferenceId);
    }
}
