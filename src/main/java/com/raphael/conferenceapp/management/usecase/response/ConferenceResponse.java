package com.raphael.conferenceapp.management.usecase.response;

import com.raphael.conferenceapp.management.entity.Conference;

import java.time.LocalDateTime;
import java.util.Collection;

public record ConferenceResponse(
        Long id,
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer participantLimit,
        Long userId,
        Collection<SessionResponse>sessions) {

    public static ConferenceResponse fromDomain(Conference conference) {
        return new ConferenceResponse(
                conference.getId(),
                conference.getTitle(),
                conference.getDescription(),
                conference.getStartTime(),
                conference.getEndTime(),
                conference.getParticipantLimit(),
                conference.getUserId(),
                conference.getSessions().stream().map(SessionResponse::fromDomain).toList()
        );
    }
}
