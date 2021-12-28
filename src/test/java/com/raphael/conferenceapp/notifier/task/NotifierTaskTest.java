package com.raphael.conferenceapp.notifier.task;

import com.raphael.conferenceapp.notifier.usecase.NotifierUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class NotifierTaskTest {

    @InjectMocks
    private NotifierTask notifierTask;

    @Mock
    private NotifierUseCase useCase;

    @Nested
    @DisplayName("method: sendNotifications()")
    class SendNotificationsMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the underlying usecase")
        void whenCalled_shouldForwardTheCallToTheUnderlyingUseCase() {
            notifierTask.sendNotifications();

            verify(useCase).sendNotificationToParticipantsForTodayConferences();
            verifyNoMoreInteractions(useCase);
        }
    }
}