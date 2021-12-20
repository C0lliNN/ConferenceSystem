package com.raphael.conferenceapp.conferencemanagement.usecase;

import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.exception.EntityNotFoundException;
import com.raphael.conferenceapp.conferencemanagement.exception.FullConferenceException;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchParticipantsRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SubscribeRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ParticipantResponse;
import lombok.AllArgsConstructor;

import javax.annotation.ManagedBean;
import java.time.Clock;

@ManagedBean
@AllArgsConstructor
public class ParticipantUseCase {
    private final ParticipantRepository participantRepository;
    private final ConferenceRepository conferenceRepository;
    private final Clock clock;

    public PaginatedItemsResponse<ParticipantResponse> getParticipants(SearchParticipantsRequest request) {
        return PaginatedItemsResponse.fromPaginatedParticipants(participantRepository.findByQuery(request.toQuery()));
    }

    public ParticipantResponse subscribe(SubscribeRequest request) {
        Conference conference = conferenceRepository.findById(request.conferenceId())
                .orElseThrow(() -> new EntityNotFoundException("Conference with ID %d was not found.", request.conferenceId()));

        if (!conference.hasCapacity()) {
            throw new FullConferenceException("The conference is already full.");
        }

        return ParticipantResponse.fromDomain(participantRepository.save(request.toParticipant(clock.instant())));
    }
}
