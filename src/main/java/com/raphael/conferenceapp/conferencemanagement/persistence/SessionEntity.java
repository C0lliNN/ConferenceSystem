package com.raphael.conferenceapp.conferencemanagement.persistence;

import com.raphael.conferenceapp.conferencemanagement.entity.Session;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private String title;
    private String description;

    @Column(name = "access_link")
    private String accessLink;

    @Column(name = "conference_id")
    private Long conferenceId;

    @ManyToOne
    private SpeakerEntity speaker;

    public Session toDomain() {
        return Session
                .builder()
                .id(id)
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .description(description)
                .accessLink(accessLink)
                .conferenceId(conferenceId)
                .speaker(Optional.ofNullable(speaker).map(SpeakerEntity::toDomain).orElse(null))
                .build();
    }

    public static SessionEntity fromDomain(Session session) {
        if (session == null) {
            return null;
        }

        return new SessionEntity(
                session.getId(),
                session.getStartTime(),
                session.getEndTime(),
                session.getTitle(),
                session.getDescription(),
                session.getAccessLink(),
                session.getConferenceId(),
                SpeakerEntity.fromDomain(session.getSpeaker())
        );
    }
}
