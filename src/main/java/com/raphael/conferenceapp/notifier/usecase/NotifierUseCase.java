package com.raphael.conferenceapp.notifier.usecase;

import com.raphael.conferenceapp.notifier.entity.Conference;
import com.raphael.conferenceapp.notifier.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ManagedBean;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;

@ManagedBean
@Slf4j
@AllArgsConstructor
public class NotifierUseCase {
    private final ConferenceManagementClient conferenceManagementClient;
    private final NotificationClient notificationClient;
    private final Clock clock;

    public void sendNotificationToParticipantsForTodayConferences() {
        LocalDate currentDate = LocalDate.now(clock);

        Collection<Conference> conferences = conferenceManagementClient.findConferencesByDate(currentDate);

        for (Conference conference : conferences) {
            Collection<Participant> participants = conferenceManagementClient.findParticipantsByConferenceId(conference.getId());

            for (Participant participant : participants) {
                try {
                    notificationClient.sendNotification(conference, participant);
                } catch (Exception e) {
                    log.warn("An error occurred when trying to send notification to participant: {}", participant.getId(), e);
                }
            }
        }
    }
}
