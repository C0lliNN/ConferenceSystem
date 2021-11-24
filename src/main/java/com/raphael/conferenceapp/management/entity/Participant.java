package com.raphael.conferenceapp.management.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Participant {
    Long id;
    String nome;
    String email;
}
