package com.raphael.conferenceapp.management.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSpeakerRepository extends JpaRepository<SpeakerEntity, Long>, JpaSpecificationExecutor<SpeakerEntity> {
}
