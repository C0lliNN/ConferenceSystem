package com.raphael.conferenceapp.notifier.usecase;

import com.raphael.conferenceapp.notifier.entity.Conference;
import com.raphael.conferenceapp.notifier.entity.Participant;

public interface NotificationClient {
    void sendNotification(Conference conference, Participant participant);
}
