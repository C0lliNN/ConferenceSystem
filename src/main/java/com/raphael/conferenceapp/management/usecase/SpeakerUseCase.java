package com.raphael.conferenceapp.management.usecase;

import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.exception.EntityNotFoundException;
import com.raphael.conferenceapp.management.usecase.request.CreateSpeakerRequest;
import com.raphael.conferenceapp.management.usecase.request.SearchSpeakersRequest;
import com.raphael.conferenceapp.management.usecase.request.UpdateSpeakerRequest;
import com.raphael.conferenceapp.management.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.management.usecase.response.SpeakerResponse;
import lombok.AllArgsConstructor;

import javax.annotation.ManagedBean;

@ManagedBean
@AllArgsConstructor
public class SpeakerUseCase {
    private SpeakerRepository repository;

    public PaginatedItemsResponse<SpeakerResponse> getSpeakers(SearchSpeakersRequest request) {
        return PaginatedItemsResponse.fromPaginatedSpeakers(repository.findByQuery(request.toQuery()));
    }

    public SpeakerResponse getSpeaker(Long speakerId) {
        return repository.findById(speakerId)
                .map(SpeakerResponse::fromDomain)
                .orElseThrow(() -> new EntityNotFoundException("Speaker with ID %d not found.", speakerId));
    }

    public SpeakerResponse createSpeaker(CreateSpeakerRequest request) {
        return SpeakerResponse.fromDomain(repository.save(request.toDomain()));
    }

    public void updateSpeaker(Long speakerId, UpdateSpeakerRequest request) {
        Speaker speaker = repository.findById(speakerId)
                .orElseThrow(() -> new EntityNotFoundException("Speaker with ID %d not found.", speakerId));

        repository.save(request.apply(speaker));
    }

    public void deleteSpeaker(Long speakerId) {
        repository.delete(speakerId);
    }
}
