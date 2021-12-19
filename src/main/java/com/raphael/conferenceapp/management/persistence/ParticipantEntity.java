package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participants")
public class ParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Column(name = "subscribed_at")
    private LocalDateTime subscribedAt;

    @Column(name = "conference_id")
    private Long conferenceId;

    public Participant toDomain() {
        return Participant.builder()
                .id(id)
                .name(name)
                .email(email)
                .subscribedAt(subscribedAt)
                .conferenceId(conferenceId)
                .build();
    }

    public static ParticipantEntity fromDomain(Participant participant) {
        return new ParticipantEntity(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getSubscribedAt(),
                participant.getConferenceId()
        );
    }
}
