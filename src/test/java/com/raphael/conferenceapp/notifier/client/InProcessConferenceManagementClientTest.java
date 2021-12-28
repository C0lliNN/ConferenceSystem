package com.raphael.conferenceapp.notifier.client;

import com.raphael.conferenceapp.conferencemanagement.mock.ConferenceMock;
import com.raphael.conferenceapp.conferencemanagement.mock.ParticipantMock;
import com.raphael.conferenceapp.conferencemanagement.usecase.ConferenceUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.ParticipantUseCase;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchConferencesRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchParticipantsRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ConferenceResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ParticipantResponse;
import com.raphael.conferenceapp.notifier.entity.Conference;
import com.raphael.conferenceapp.notifier.entity.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InProcessConferenceManagementClientTest {

    @Mock
    private ConferenceUseCase conferenceUseCase;

    @Mock
    private ParticipantUseCase participantUseCase;

    @InjectMocks
    private InProcessConferenceManagementClient client;

    @Nested
    @DisplayName("method: findConferenceByDate(LocalDate)")
    class FindConferenceByDateMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the underlying usecase")
        void whenCalled_shouldForwardTheCallToTheUnderlyingUseCase() {
            LocalDate date = LocalDate.of(2021, Month.DECEMBER, 24);
            LocalDateTime startTime = LocalDateTime.of(date, LocalTime.of(1, 0));
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.of(22, 0));

            SearchConferencesRequest request = new SearchConferencesRequest(null, startTime, endTime, null, 1000L, 1L);

            ConferenceResponse conference1 = ConferenceMock.newConferenceResponse();
            ConferenceResponse conference2 = ConferenceMock.newConferenceResponse();

            PaginatedItemsResponse<ConferenceResponse> paginatedItems = new PaginatedItemsResponse<>(
                    List.of(conference1, conference2),
                    2L,
                    2L,
                    2L,
                    2L
            );

            when(conferenceUseCase.getConferences(request)).thenReturn(paginatedItems);

            Conference expectedConference1 = Conference
                    .builder()
                    .id(conference1.id())
                    .title(conference1.title())
                    .description(conference1.description())
                    .startTime(conference1.startTime())
                    .build();

            Conference expectedConference2 = Conference
                    .builder()
                    .id(conference2.id())
                    .title(conference2.title())
                    .description(conference2.description())
                    .startTime(conference2.startTime())
                    .build();

            assertThat(client.findConferencesByDate(date)).containsExactly(expectedConference1, expectedConference2);

            verify(conferenceUseCase).getConferences(request);
            verifyNoMoreInteractions(conferenceUseCase);

            verifyNoInteractions(participantUseCase);
        }
    }

    @Nested
    @DisplayName("method: findParticipantsByConferenceId(Long)")
    class FindParticipantsByConferenceIdMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the usecase")
        void whenCalled_shouldForwardTheCallToTheUseCase() {
            Long conferenceId = 4L;

            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, conferenceId, 1000L, 1L);

            ParticipantResponse participant1 = ParticipantMock.newResponse();
            ParticipantResponse participant2 = ParticipantMock.newResponse();

            PaginatedItemsResponse<ParticipantResponse> paginatedItems = new PaginatedItemsResponse<>(
                    List.of(participant1, participant2),
                    2L,
                    2L,
                    2L,
                    2L
            );

            when(participantUseCase.getParticipants(request)).thenReturn(paginatedItems);

            Participant expectedParticipant1 = Participant.builder()
                    .id(participant1.id())
                    .name(participant1.name())
                    .email(participant1.email())
                    .build();

            Participant expectedParticipant2 = Participant.builder()
                    .id(participant2.id())
                    .name(participant2.name())
                    .email(participant2.email())
                    .build();

            assertThat(client.findParticipantsByConferenceId(conferenceId))
                    .containsExactly(expectedParticipant1, expectedParticipant2);

            verify(participantUseCase).getParticipants(request);
            verifyNoMoreInteractions(participantUseCase);

            verifyNoInteractions(conferenceUseCase);
        }
    }
}