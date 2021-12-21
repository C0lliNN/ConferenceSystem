package com.raphael.conferenceapp.conferencemanagement.web;

import com.raphael.conferenceapp.conferencemanagement.usecase.ConferenceUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.ParticipantUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchConferencesRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SubscribeRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ConferenceResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ParticipantResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/participant")
@Tag(name = "Participant")
public class ParticipantController {
    private final ConferenceUseCase conferenceUseCase;
    private final ParticipantUseCase participantUseCase;

    @GetMapping("/conferences")
    public PaginatedItemsResponse<ConferenceResponse> getConferences(SearchConferencesRequest request) {
        return conferenceUseCase.getConferences(request);
    }

    @GetMapping("/conferences/{conferenceId}")
    public ConferenceResponse getConference(@PathVariable("conferenceId") Long conferenceId) {
        return conferenceUseCase.getConference(conferenceId);
    }

    @PostMapping("/conferences/{conferenceId}")
    public ParticipantResponse subscribe(@PathVariable("conferenceId") Long conferenceId, @Valid @RequestBody SubscribeRequest request) {
        return participantUseCase.subscribe(request.withConferenceId(conferenceId));
    }
}
