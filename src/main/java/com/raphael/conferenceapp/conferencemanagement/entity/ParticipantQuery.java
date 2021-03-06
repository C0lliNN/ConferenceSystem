package com.raphael.conferenceapp.conferencemanagement.entity;

public record ParticipantQuery(
        String name,
        String email,
        Long conferenceId,
        Long limit,
        Long offset) {

    public Long currentPage() {
        return (offset / limit) + 1;
    }
}
