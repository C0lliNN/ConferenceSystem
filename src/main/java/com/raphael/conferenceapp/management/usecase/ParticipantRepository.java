package com.raphael.conferenceapp.management.usecase;

import com.raphael.conferenceapp.management.entity.PaginatedItems;
import com.raphael.conferenceapp.management.entity.Participant;
import com.raphael.conferenceapp.management.entity.ParticipantQuery;

public interface ParticipantRepository {
    PaginatedItems<Participant> findByQuery(ParticipantQuery query);
}
