package com.raphael.conferenceapp.notifier.client;

import com.raphael.conferenceapp.notifier.entity.Conference;
import com.raphael.conferenceapp.notifier.entity.Participant;
import com.raphael.conferenceapp.notifier.usecase.NotificationClient;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailClient implements NotificationClient {
    private final JavaMailSender emailSender;

    @Override
    public void sendNotification(final Conference conference, final Participant participant) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@conferenceapp.com");

        message.setTo(participant.getEmail());
        message.setSubject(conference.getTitle() + " Reminder");

        String text =
                String.format("Hello, %s!\n\nToday starts %s. We're looking forward to welcome you there.", participant.getName(), conference.getTitle());
        message.setText(text);

        emailSender.send(message);
    }
}
