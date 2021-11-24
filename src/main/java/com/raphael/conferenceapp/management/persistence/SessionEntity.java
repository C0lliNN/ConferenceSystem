package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Session;
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
    Long id;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    String title;
    String description;

    @Column(name = "access_link")
    String accessLink;

    @ManyToOne
    SpeakerEntity speaker;

    public Session toDomain() {
        return Session
                .builder()
                .id(id)
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .description(description)
                .accessLink(accessLink)
                .speaker(speaker.toDomain())
                .build();
    }

    public static SessionEntity fromDomain(Session session) {
        return new SessionEntity(
                session.getId(),
                session.getStartTime(),
                session.getEndTime(),
                session.getTitle(),
                session.getDescription(),
                session.getAccessLink(),
                SpeakerEntity.fromDomain(session.getSpeaker())
        );
    }
}
