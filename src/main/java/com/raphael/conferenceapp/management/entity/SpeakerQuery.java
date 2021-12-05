package com.raphael.conferenceapp.management.entity;

public record SpeakerQuery(String name, String email, Long limit, Long offset) {

    public Long currentPage() {
        return (offset / limit) + 1;
    }
}
