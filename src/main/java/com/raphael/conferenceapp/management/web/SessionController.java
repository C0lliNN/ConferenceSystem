package com.raphael.conferenceapp.management.web;

import com.raphael.conferenceapp.management.usecase.SessionUseCase;
import com.raphael.conferenceapp.management.usecase.request.CreateSessionRequest;
import com.raphael.conferenceapp.management.usecase.request.UpdateSessionRequest;
import com.raphael.conferenceapp.management.usecase.response.SessionResponse;
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
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Session")
public class SessionController {
    private final SessionUseCase sessionUseCase;

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
}
