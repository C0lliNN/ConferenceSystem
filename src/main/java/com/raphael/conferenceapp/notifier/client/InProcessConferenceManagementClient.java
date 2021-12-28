package com.raphael.conferenceapp.notifier.client;

import com.raphael.conferenceapp.conferencemanagement.usecase.ConferenceUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.ParticipantUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchConferencesRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchParticipantsRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ConferenceResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.notifier.entity.Conference;
import com.raphael.conferenceapp.notifier.entity.Participant;
import com.raphael.conferenceapp.notifier.usecase.ConferenceManagementClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

@Component
@AllArgsConstructor
public class InProcessConferenceManagementClient implements ConferenceManagementClient {
    private final ConferenceUseCase conferenceUseCase;
    private final ParticipantUseCase participantUseCase;

    @Override
    public Collection<Conference> findConferencesByDate(final LocalDate date) {
        LocalDateTime startTime = LocalDateTime.of(date, LocalTime.of(1, 0));
        LocalDateTime endTime = LocalDateTime.of(date, LocalTime.of(22, 0));

        SearchConferencesRequest request = new SearchConferencesRequest(null, startTime, endTime, null, 1000L, 1L);

        PaginatedItemsResponse<ConferenceResponse> paginatedItems = conferenceUseCase.getConferences(request);

        return paginatedItems.results()
                .stream()
                .map(conference -> Conference.builder()
                        .id(conference.id())
                        .title(conference.title())
                        .description(conference.description())
                        .startTime(conference.startTime())
                        .build()
                )
                .toList();
    }

    @Override
    public Collection<Participant> findParticipantsByConferenceId(final Long conferenceId) {
        SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, conferenceId, 1000L, 1L);

        return participantUseCase.getParticipants(request).results()
                .stream()
                .map(participant -> Participant.builder()
                        .id(participant.id())
                        .name(participant.name())
                        .email(participant.email())
                        .build()
                )
                .toList();
    }
}
