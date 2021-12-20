package com.raphael.conferenceapp.conferencemanagement.usecase;

import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;
import com.raphael.conferenceapp.conferencemanagement.entity.SpeakerQuery;

import java.util.Optional;

public interface SpeakerRepository {
    PaginatedItems<Speaker> findByQuery(SpeakerQuery query);
    Optional<Speaker> findById(Long id);
    Speaker save(Speaker speaker);
    void delete(Long id);
}
