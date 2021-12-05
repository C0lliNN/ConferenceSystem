package com.raphael.conferenceapp.management.usecase;

import com.raphael.conferenceapp.management.entity.PaginatedItems;
import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.entity.SpeakerQuery;

import java.util.Optional;

public interface SpeakerRepository {
    PaginatedItems<Speaker> findByQuery(SpeakerQuery query);
    Optional<Speaker> findById(Long id);
    Speaker save(Speaker speaker);
    void delete(Long id);
}
