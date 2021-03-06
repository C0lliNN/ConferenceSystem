package com.raphael.conferenceapp.conferencemanagement.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSessionRepository extends JpaRepository<SessionEntity, Long> {
    List<SessionEntity> findByConferenceId(Long conferenceId);
}
