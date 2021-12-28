package com.raphael.conferenceapp.notifier.task;

import com.raphael.conferenceapp.notifier.usecase.NotifierUseCase;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class NotifierTask {
    private final NotifierUseCase notifierUseCase;

    @Transactional
    @Scheduled(fixedRate = 24, timeUnit = TimeUnit.HOURS)
    public void sendNotifications() {
        notifierUseCase.sendNotificationToParticipantsForTodayConferences();
    }
}
