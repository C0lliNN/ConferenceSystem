package com.raphael.conferenceapp.conferencemanagement.usecase;

import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.entity.ConferenceQuery;
import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;

import java.util.Optional;

public interface ConferenceRepository {
    PaginatedItems<Conference> findByQuery(ConferenceQuery query);
    Optional<Conference> findById(Long conferenceId);
    Conference save(Conference conference);
    void delete(Long conferenceId);
}
