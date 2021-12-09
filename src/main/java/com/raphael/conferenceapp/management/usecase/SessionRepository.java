package com.raphael.conferenceapp.management.usecase;

import com.raphael.conferenceapp.management.entity.Session;

import java.util.Collection;

public interface SessionRepository {
    Collection<Session> findByConferenceId(Long conferenceId);
    Session save(Session session);
    void delete(Long sessionId);
}
