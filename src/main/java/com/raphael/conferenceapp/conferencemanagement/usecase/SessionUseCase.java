package com.raphael.conferenceapp.conferencemanagement.usecase;

import com.raphael.conferenceapp.conferencemanagement.entity.Session;
import com.raphael.conferenceapp.conferencemanagement.exception.EntityNotFoundException;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateSessionRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateSessionRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.SessionResponse;
import lombok.AllArgsConstructor;

import javax.annotation.ManagedBean;
import java.util.Collection;

@ManagedBean
@AllArgsConstructor
public class SessionUseCase {
    private SessionRepository repository;

    public Collection<SessionResponse> getSessionsByConferenceId(Long conferenceId) {
        return repository.findByConferenceId(conferenceId)
                .stream()
                .map(SessionResponse::fromDomain)
                .toList();
    }

    public SessionResponse createSession(CreateSessionRequest request) {
        return SessionResponse.fromDomain(repository.save(request.toDomain()));
    }

    public void updateSession(Long sessionId, UpdateSessionRequest request) {
        Session session = repository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session with ID %d was not found.", sessionId));

        repository.save(request.apply(session));
    }

    public void deleteSession(Long sessionId) {
        repository.delete(sessionId);
    }
}
