package com.raphael.conferenceapp.management.usecase;

import com.raphael.conferenceapp.management.entity.Session;

import java.util.Collection;
import java.util.Optional;

public interface SessionRepository {
    Collection<Session> findByConferenceId(Long conferenceId);
    Optional<Session> findById(Long sessionId);
    Session save(Session session);
    void delete(Long sessionId);
}
