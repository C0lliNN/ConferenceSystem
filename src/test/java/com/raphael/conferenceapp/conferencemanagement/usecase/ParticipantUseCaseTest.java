package com.raphael.conferenceapp.conferencemanagement.usecase;

import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.mock.ParticipantMock;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchParticipantsRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ParticipantResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantUseCaseTest {

    @InjectMocks
    private ParticipantUseCase useCase;

    @Mock
    private ParticipantRepository repository;

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

            when(repository.findByQuery(request.toQuery())).thenReturn(paginatedItems);

            PaginatedItemsResponse<ParticipantResponse> expected = PaginatedItemsResponse.fromPaginatedParticipants(paginatedItems);
            PaginatedItemsResponse<ParticipantResponse> actual = useCase.getParticipants(request);

            assertThat(actual).isEqualTo(expected);

            verify(repository).findByQuery(request.toQuery());
            verifyNoMoreInteractions(repository);
        }
    }
}