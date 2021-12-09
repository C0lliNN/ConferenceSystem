package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Session;
import com.raphael.conferenceapp.management.usecase.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@AllArgsConstructor
@Component
public class SqlSessionRepository implements SessionRepository {
    private final JPASessionRepository jpaRepository;

    @Override
    public Collection<Session> findByConferenceId(final Long conferenceId) {
        return jpaRepository.findByConferenceId(conferenceId)
                .stream()
                .map(SessionEntity::toDomain)
                .toList();
    }

    @Override
    public Session save(final Session session) {
        return jpaRepository.save(SessionEntity.fromDomain(session)).toDomain();
    }

    @Override
    public void delete(final Long sessionId) {
        jpaRepository.deleteById(sessionId);
    }
}
