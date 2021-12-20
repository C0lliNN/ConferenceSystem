package com.raphael.conferenceapp.conferencemanagement.web;

import com.raphael.conferenceapp.auth.entity.User;
import com.raphael.conferenceapp.conferencemanagement.usecase.ConferenceUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateConferenceRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchConferencesRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateConferenceRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ConferenceResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
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

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Conference")
public class ConferenceController {
    private final ConferenceUseCase conferenceUseCase;

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
}
