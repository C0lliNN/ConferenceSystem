package com.raphael.conferenceapp.management.entity;

import java.time.LocalDateTime;

public record ConferenceQuery(
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long userId,
        Long limit,
        Long offset) {

    public Long currentPage() {
        return (offset / limit) + 1;
    }
}
