package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Conference;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conferences")
public class ConferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany
    @ToString.Exclude
    private Collection<SessionEntity> sessions;

    public Conference toDomain() {
        List<Session> sessions = Objects.requireNonNullElse(this.sessions, Collections.<SessionEntity>emptyList())
                .stream()
                .map(SessionEntity::toDomain)
                .toList();

        return Conference
                .builder()
                .id(id)
                .title(title)
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .participantLimit(participantLimit)
                .userId(userId)
                .sessions(sessions)
                .build();
    }

    public static ConferenceEntity fromDomain(Conference conference) {
        if (conference == null) {
            return null;
        }

        List<SessionEntity> sessions = Objects.requireNonNullElse(conference.getSessions(), Collections.<Session>emptyList())
                .stream()
                .map(SessionEntity::fromDomain)
                .toList();

        return new ConferenceEntity(
                conference.getId(),
                conference.getTitle(),
                conference.getDescription(),
                conference.getStartTime(),
                conference.getEndTime(),
                conference.getParticipantLimit(),
                conference.getUserId(),
                sessions
        );
    }
}
