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
import java.util.List;

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
    Long id;
    String title;
    String description;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @Column(name = "participant_limit")
    Integer participantLimit;

    @Column(name = "user_id")
    Long userId;

    @OneToMany
    @ToString.Exclude
    Collection<SessionEntity> sessions;

    public Conference toDomain() {
        List<Session> sessions = this.sessions.stream()
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
        return new ConferenceEntity(
                conference.getId(),
                conference.getTitle(),
                conference.getDescription(),
                conference.getStartTime(),
                conference.getEndTime(),
                conference.getParticipantLimit(),
                conference.getUserId(),
                conference.getSessions().stream().map(SessionEntity::fromDomain).toList()
        );
    }
}
