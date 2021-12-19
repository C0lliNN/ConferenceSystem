package com.raphael.conferenceapp.management.web;

import com.raphael.conferenceapp.management.usecase.ParticipantUseCase;
import com.raphael.conferenceapp.management.usecase.request.SearchParticipantsRequest;
import com.raphael.conferenceapp.management.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.management.usecase.response.ParticipantResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Participant")
public class ParticipantController {
    private final ParticipantUseCase participantUseCase;

    @GetMapping("/conferences/{id}/participants")
    public PaginatedItemsResponse<ParticipantResponse> getParticipants(@PathVariable("id") Long conferenceId,
                                                                       SearchParticipantsRequest request) {
        return participantUseCase.getParticipants(request.withConferenceId(conferenceId));
    }
}
