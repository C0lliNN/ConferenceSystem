package com.raphael.conferenceapp.conferencemanagement.usecase;

import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.exception.EntityNotFoundException;
import com.raphael.conferenceapp.conferencemanagement.exception.FullConferenceException;
import com.raphael.conferenceapp.conferencemanagement.mock.ParticipantMock;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchParticipantsRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SubscribeRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ParticipantResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantUseCaseTest {

    @InjectMocks
    private ParticipantUseCase useCase;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ConferenceRepository conferenceRepository;

    @Mock
    private Clock clock;

    @Nested
    @DisplayName("method: getParticipants(SearchParticipantsRequest)")
    class GetParticipantsMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the repository")
        void whenCalled_shouldForwardTheCallToTheRepository() {
            SearchParticipantsRequest request = new SearchParticipantsRequest(null, null, null, null, null);

            Participant speaker1 = ParticipantMock.newParticipantDomain();
            Participant speaker2 = ParticipantMock.newParticipantDomain();

            PaginatedItems<Participant> paginatedItems = new PaginatedItems<>(
                    List.of(speaker1, speaker2),
                    10L,
                    2L,
                    0L
            );

            when(participantRepository.findByQuery(request.toQuery())).thenReturn(paginatedItems);

            PaginatedItemsResponse<ParticipantResponse> expected = PaginatedItemsResponse.fromPaginatedParticipants(paginatedItems);
            PaginatedItemsResponse<ParticipantResponse> actual = useCase.getParticipants(request);

            assertThat(actual).isEqualTo(expected);

            verify(participantRepository).findByQuery(request.toQuery());
            verifyNoMoreInteractions(participantRepository);

            verifyNoMoreInteractions(conferenceRepository, clock);
        }
    }

    @Nested
    @DisplayName("method: subscribe(SubscribeRequest)")
    class SubscribeMethod {

        @Test
        @DisplayName("when conference is not found, then it should return an error")
        void whenConferenceIsNotFound_shouldReturnAnError() {
            SubscribeRequest request = ParticipantMock.newSubscribeRequest();

            when(conferenceRepository.findById(request.conferenceId())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.subscribe(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Conference with ID %d was not found.", request.conferenceId());

            verify(conferenceRepository).findById(request.conferenceId());
            verifyNoInteractions(participantRepository, clock);
        }

        @Test
        @DisplayName("when conference is full, then it should return an error")
        void whenConferenceIsFull_shouldReturnAnError() {
            SubscribeRequest request = ParticipantMock.newSubscribeRequest();
            Conference conference = Conference.builder()
                    .totalParticipants(10)
                    .participantLimit(10)
                    .build();

            when(conferenceRepository.findById(request.conferenceId())).thenReturn(Optional.of(conference));

            assertThatThrownBy(() -> useCase.subscribe(request))
                    .isInstanceOf(FullConferenceException.class)
                    .hasMessage("The conference is already full.");

            verify(conferenceRepository).findById(request.conferenceId());
            verifyNoInteractions(participantRepository, clock);
        }

        @Test
        @DisplayName("when conference is valid, then it should forward the call to the underlying repository")
        void whenConferenceIsValid_shouldForwardTheCallToTheUnderlyingRepository() {
            SubscribeRequest request = ParticipantMock.newSubscribeRequest();
            Conference conference = Conference.builder()
                    .totalParticipants(5)
                    .participantLimit(10)
                    .build();

            when(conferenceRepository.findById(request.conferenceId())).thenReturn(Optional.of(conference));

            Instant now = Instant.now();
            when(clock.instant()).thenReturn(now);

            Participant participant = request.toParticipant(now);
            when(participantRepository.save(participant)).thenReturn(participant);

            ParticipantResponse expected = ParticipantResponse.fromDomain(participant);
            ParticipantResponse actual = useCase.subscribe(request);

            assertThat(actual).isEqualTo(expected);

            verify(conferenceRepository).findById(request.conferenceId());
            verify(clock).instant();
            verify(participantRepository).save(participant);

            verifyNoMoreInteractions(conferenceRepository, clock);
        }
    }
}