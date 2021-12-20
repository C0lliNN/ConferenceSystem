package com.raphael.conferenceapp.conferencemanagement.usecase;

import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.entity.ParticipantQuery;

public interface ParticipantRepository {
    PaginatedItems<Participant> findByQuery(ParticipantQuery query);
    Participant save(Participant participant);
}
