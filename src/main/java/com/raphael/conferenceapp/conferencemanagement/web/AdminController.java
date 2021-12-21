package com.raphael.conferenceapp.conferencemanagement.web;

import com.raphael.conferenceapp.auth.entity.User;
import com.raphael.conferenceapp.conferencemanagement.usecase.ConferenceUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.ParticipantUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.SessionUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.SpeakerUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateConferenceRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateSessionRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateSpeakerRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchConferencesRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchParticipantsRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchSpeakersRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateConferenceRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateSessionRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateSpeakerRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ConferenceResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ParticipantResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.SessionResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.SpeakerResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin")
public class AdminController {
    private final ConferenceUseCase conferenceUseCase;
    private final ParticipantUseCase participantUseCase;
    private final SessionUseCase sessionUseCase;
    private final SpeakerUseCase speakerUseCase;

    @GetMapping("/conferences")
    public PaginatedItemsResponse<ConferenceResponse> getConferences(SearchConferencesRequest request) {
        return conferenceUseCase.getConferences(request);
    }

    @GetMapping("/conferences/{conferenceId}")
    public ConferenceResponse getConference(@PathVariable("conferenceId") Long conferenceId) {
        return conferenceUseCase.getConference(conferenceId);
    }

    @PostMapping("/conferences")
    @ResponseStatus(HttpStatus.CREATED)
    public ConferenceResponse createConference(@RequestBody @Valid CreateConferenceRequest request,
                                               @AuthenticationPrincipal User user) {
        return conferenceUseCase.createConference(request, user.getId());
    }

    @PatchMapping("/conferences/{conferenceId}")
    public void updateConference(@PathVariable("conferenceId") Long conferenceId, @RequestBody @Valid UpdateConferenceRequest request) {
        conferenceUseCase.updateConference(conferenceId, request);
    }

    @DeleteMapping("/conferences/{conferenceId}")
    public void deleteConference(@PathVariable("conferenceId") Long conferenceId) {
        conferenceUseCase.deleteConference(conferenceId);
    }

    @GetMapping("/conferences/{id}/participants")
    public PaginatedItemsResponse<ParticipantResponse> getParticipants(@PathVariable("id") Long conferenceId,
                                                                       SearchParticipantsRequest request) {
        return participantUseCase.getParticipants(request.withConferenceId(conferenceId));
    }

    @GetMapping("/conferences/{conferenceId}/sessions")
    public Collection<SessionResponse> getSessions(@PathVariable("conferenceId") Long conferenceId) {
        return sessionUseCase.getSessionsByConferenceId(conferenceId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sessions")
    public SessionResponse createSession(@RequestBody @Valid CreateSessionRequest request) {
        return sessionUseCase.createSession(request);
    }

    @PatchMapping("/sessions/{sessionId}")
    public void updateSession(@PathVariable("sessionId") Long sessionId, @RequestBody @Valid UpdateSessionRequest request) {
        sessionUseCase.updateSession(sessionId, request);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public void deleteSession(@PathVariable("sessionId") Long sessionId) {
        sessionUseCase.deleteSession(sessionId);
    }

    @GetMapping("/speakers")
    public PaginatedItemsResponse<SpeakerResponse> getSpeakers(SearchSpeakersRequest request) {
        return speakerUseCase.getSpeakers(request);
    }

    @GetMapping("/speakers/{speakerId}")
    public SpeakerResponse getSpeaker(@PathVariable("speakerId") Long speakerId) {
        return speakerUseCase.getSpeaker(speakerId);
    }

    @PostMapping("/speakers")
    @ResponseStatus(HttpStatus.CREATED)
    public SpeakerResponse createSpeaker(@RequestBody @Valid CreateSpeakerRequest request) {
        return speakerUseCase.createSpeaker(request);
    }

    @PatchMapping("/speakers/{speakerId}")
    public void updateSpeaker(@PathVariable("speakerId") Long speakerId, @RequestBody @Valid UpdateSpeakerRequest request) {
        speakerUseCase.updateSpeaker(speakerId, request);
    }

    @DeleteMapping("/speakers/{speakerId}")
    public void deleteSpeaker(@PathVariable("speakerId") Long speakerId) {
        speakerUseCase.deleteSpeaker(speakerId);
    }
}
