package com.raphael.conferenceapp.conferencemanagement.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Speaker {
    Long id;
    String firstName;
    String lastName;
    String email;
    String professionalTitle;
}
