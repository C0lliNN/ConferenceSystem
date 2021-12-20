package com.raphael.conferenceapp.conferencemanagement.usecase.response;

import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;

import java.util.Collection;

public record PaginatedItemsResponse<T>(
        Collection<T> results,
        Long currentPage,
        Long perPage,
        Long totalItems,
        Long totalPages) {

    public static PaginatedItemsResponse<ConferenceResponse> fromPaginatedConferences(PaginatedItems<Conference> paginatedItems) {
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

    public static PaginatedItemsResponse<SpeakerResponse> fromPaginatedSpeakers(PaginatedItems<Speaker> paginatedItems) {
        Collection<SpeakerResponse> speakers = paginatedItems.items()
                .stream()
                .map(SpeakerResponse::fromDomain)
                .toList();

        return new PaginatedItemsResponse<>(
                speakers,
                paginatedItems.currentPage(),
                paginatedItems.limit(),
                paginatedItems.totalItems(),
                paginatedItems.totalPages()
        );
    }

    public static PaginatedItemsResponse<ParticipantResponse> fromPaginatedParticipants(PaginatedItems<Participant> paginatedItems) {
        Collection<ParticipantResponse> participants = paginatedItems.items()
                .stream()
                .map(ParticipantResponse::fromDomain)
                .toList();

        return new PaginatedItemsResponse<>(
                participants,
                paginatedItems.currentPage(),
                paginatedItems.limit(),
                paginatedItems.totalItems(),
                paginatedItems.totalPages()
        );
    }
}
