package com.raphael.conferenceapp.management.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAConferenceRepository extends JpaRepository<ConferenceEntity, Long>,
        JpaSpecificationExecutor<ConferenceEntity> {

    @EntityGraph(attributePaths = "sessions")
    Optional<ConferenceEntity> findOneWithSessionsById(@Param("id") Long id);
}
