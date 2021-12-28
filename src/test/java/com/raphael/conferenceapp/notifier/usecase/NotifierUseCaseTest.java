package com.raphael.conferenceapp.notifier.usecase;

import com.raphael.conferenceapp.notifier.entity.Conference;
import com.raphael.conferenceapp.notifier.entity.Participant;
import com.raphael.conferenceapp.notifier.mock.NotifierMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotifierUseCaseTest {

    @InjectMocks
    private NotifierUseCase notifierUseCase;

    @Mock
    private ConferenceManagementClient conferenceManagementClient;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private Clock clock;

    @Nested
    @DisplayName("method: sendNotificationToParticipantsForTodayConferences()")
    class SendNotificationsMethod {

        @Test
        @DisplayName("when called, then it should fetch all the conferences for the current date and notify the participants")
        void whenCalled_shouldFetchAllTheConferencesForTheCurrentDateAndNotifyTheParticipants() {
            LocalDate date = LocalDate.of(2021, Month.DECEMBER, 28);
            when(clock.instant()).thenReturn(Instant.ofEpochSecond(date.toEpochSecond(LocalTime.of(1, 30), ZoneOffset.UTC)));
            when(clock.getZone()).thenReturn(ZoneOffset.UTC);


            Conference conference = NotifierMock.newConference();
            when(conferenceManagementClient.findConferencesByDate(date)).thenReturn(List.of(conference));

            Participant participant1 = NotifierMock.newParticipant();
            Participant participant2 = NotifierMock.newParticipant();
            when(conferenceManagementClient.findParticipantsByConferenceId(conference.getId())).thenReturn(List.of(participant1, participant2));

            notifierUseCase.sendNotificationToParticipantsForTodayConferences();

            verify(notificationClient).sendNotification(conference, participant1);
            verify(notificationClient).sendNotification(conference, participant2);
        }
    }
}