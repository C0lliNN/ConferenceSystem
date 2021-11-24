package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Speaker;
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

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "speakers")
public class SpeakerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    String email;

    @Column(name = "professional_title")
    String professionalTitle;

    public Speaker toDomain() {
        return Speaker
                .builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .professionalTitle(professionalTitle)
                .build();
    }

    public static SpeakerEntity fromDomain(Speaker speaker) {
        return new SpeakerEntity(
                speaker.getId(),
                speaker.getFirstName(),
                speaker.getLastName(),
                speaker.getEmail(),
                speaker.getProfessionalTitle()
        );
    }
}
