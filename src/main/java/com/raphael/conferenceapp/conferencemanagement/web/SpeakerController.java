package com.raphael.conferenceapp.conferencemanagement.web;

import com.raphael.conferenceapp.conferencemanagement.usecase.SpeakerUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateSpeakerRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchSpeakersRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateSpeakerRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.SpeakerResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Speaker")
public class SpeakerController {
    private final SpeakerUseCase speakerUseCase;

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
