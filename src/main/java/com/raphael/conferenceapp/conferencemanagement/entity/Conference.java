package com.raphael.conferenceapp.conferencemanagement.entity;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Value
@Builder(toBuilder = true)
public class Conference {
    Long id;
    String title;
    String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Integer totalParticipants;
    Integer participantLimit;
    Long userId;
    Collection<Session> sessions;

    public Collection<Session> getSessions() {
        if (sessions == null) {
            return Collections.emptyList();
        }

        return sessions;
    }

    public boolean hasSessions() {
        return !getSessions().isEmpty();
    }
}
