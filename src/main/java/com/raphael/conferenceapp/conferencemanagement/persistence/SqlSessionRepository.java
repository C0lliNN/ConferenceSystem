package com.raphael.conferenceapp.conferencemanagement.persistence;

import com.raphael.conferenceapp.conferencemanagement.entity.Session;
import com.raphael.conferenceapp.conferencemanagement.exception.EntityNotFoundException;
import com.raphael.conferenceapp.conferencemanagement.usecase.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Component
public class SqlSessionRepository implements SessionRepository {
    private final JpaSessionRepository jpaRepository;

    @Override
    public Collection<Session> findByConferenceId(final Long conferenceId) {
        return jpaRepository.findByConferenceId(conferenceId)
                .stream()
                .map(SessionEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Session> findById(Long sessionId) {
        return jpaRepository.findById(sessionId).map(SessionEntity::toDomain);
    }

    @Override
    public Session save(final Session session) {
        return jpaRepository.save(SessionEntity.fromDomain(session)).toDomain();
    }

    @Override
    public void delete(final Long sessionId) {
        try {
            jpaRepository.deleteById(sessionId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Session with ID %d was not found.", sessionId);
        }
    }
}
