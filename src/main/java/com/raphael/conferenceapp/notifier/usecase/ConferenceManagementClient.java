package com.raphael.conferenceapp.notifier.usecase;

import com.raphael.conferenceapp.notifier.entity.Conference;
import com.raphael.conferenceapp.notifier.entity.Participant;

import java.time.LocalDate;
import java.util.Collection;

public interface ConferenceManagementClient {
    Collection<Conference> findConferencesByDate(LocalDate date);
    Collection<Participant> findParticipantsByConferenceId(Long conferenceId);
}
