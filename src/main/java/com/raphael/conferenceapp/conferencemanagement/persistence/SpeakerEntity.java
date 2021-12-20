package com.raphael.conferenceapp.conferencemanagement.persistence;

import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;
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
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "professional_title")
    private String professionalTitle;

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
        if (speaker == null) {
            return null;
        }

        return new SpeakerEntity(
                speaker.getId(),
                speaker.getFirstName(),
                speaker.getLastName(),
                speaker.getEmail(),
                speaker.getProfessionalTitle()
        );
    }
}
