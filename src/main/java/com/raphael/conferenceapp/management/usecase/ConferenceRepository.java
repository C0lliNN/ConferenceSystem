package com.raphael.conferenceapp.management.usecase;

import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.entity.ConferenceQuery;
import com.raphael.conferenceapp.management.entity.PaginatedItems;

import java.util.Optional;

public interface ConferenceRepository {
    PaginatedItems<Conference> findByQuery(ConferenceQuery query);
    Optional<Conference> findById(Long conferenceId);
    Conference save(Conference conference);
    void delete(Long conferenceId);
}
