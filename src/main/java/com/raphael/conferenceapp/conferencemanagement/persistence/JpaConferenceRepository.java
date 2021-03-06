package com.raphael.conferenceapp.conferencemanagement.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaConferenceRepository extends JpaRepository<ConferenceEntity, Long>,
        JpaSpecificationExecutor<ConferenceEntity> {
}
