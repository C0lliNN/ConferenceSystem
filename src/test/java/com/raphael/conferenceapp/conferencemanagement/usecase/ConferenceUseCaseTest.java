package com.raphael.conferenceapp.conferencemanagement.usecase;

import com.raphael.conferenceapp.conferencemanagement.entity.Conference;
import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.exception.DeletionConflictException;
import com.raphael.conferenceapp.conferencemanagement.exception.EntityNotFoundException;
import com.raphael.conferenceapp.conferencemanagement.mock.ConferenceMock;
import com.raphael.conferenceapp.conferencemanagement.mock.SessionMock;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateConferenceRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchConferencesRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateConferenceRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.ConferenceResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConferenceUseCaseTest {

    @InjectMocks
    private ConferenceUseCase useCase;

    @Mock
    private ConferenceRepository repository;

    @Nested
    @DisplayName("method: getConferences(SearchConferencesRequest)")
    class GetConferencesMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the repository")
        void whenCalled_shouldForwardTheCallToTheRepository() {
            SearchConferencesRequest request = new SearchConferencesRequest("some title", null, null, null, null, null);

            Conference conference1 = ConferenceMock.newConferenceDomain();
            Conference conference2 = ConferenceMock.newConferenceDomain();

            PaginatedItems<Conference> paginatedItems = new PaginatedItems<>(
                    List.of(conference1, conference2),
                    10L,
                    2L,
                    0L
            );

            when(repository.findByQuery(request.toQuery())).thenReturn(paginatedItems);

            PaginatedItemsResponse<ConferenceResponse> expected = PaginatedItemsResponse.fromPaginatedConferences(paginatedItems);
            PaginatedItemsResponse<ConferenceResponse> actual = useCase.getConferences(request);

            assertThat(actual).isEqualTo(expected);

            verify(repository).findByQuery(request.toQuery());
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: getConference(Long)")
    class GetConferenceMethod {

        @Test
        @DisplayName("when conference is not found, then it should throw an EntityNotFoundException")
        void whenConferenceIsNotFound_shouldThrowAnEntityNotFoundException() {
            when(repository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.getConference(1L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Conference with ID %d was not found.", 1L);

            verify(repository).findById(1L);
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("when conference is found, then it should return it")
        void whenConferenceIsFound_shouldReturnIt() {
            Conference conference = ConferenceMock.newConferenceDomain();

            when(repository.findById(conference.getId())).thenReturn(Optional.of(conference));

            ConferenceResponse expected = ConferenceResponse.fromDomain(conference);
            ConferenceResponse actual = useCase.getConference(conference.getId());

            assertThat(actual).isEqualTo(expected);

            verify(repository).findById(conference.getId());
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: createConference(CreateConferenceRequest, Long)")
    class CreateConferenceMethod {

        @Test
        @DisplayName("when called, then it should forward the call to underlying repository")
        void whenCalled_shouldForwardTheCallToUnderlyingRepository() {
            CreateConferenceRequest request = new CreateConferenceRequest("some title", null, null, null, null);
            Long userId = 1L;

            Conference returnedConference = request.toDomain(userId).toBuilder().id(2L).build();
            when(repository.save(request.toDomain(userId))).thenReturn(returnedConference);

            ConferenceResponse expected = ConferenceResponse.fromDomain(returnedConference);
            ConferenceResponse actual = useCase.createConference(request, userId);

            assertThat(actual).isEqualTo(expected);

            verify(repository).save(request.toDomain(userId));
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: updateConference(Long, UpdateConferenceRequest)")
    class UpdateConferenceMethod {

        @Test
        @DisplayName("when conference is not found, then it should throw an EntityNotFoundException")
        void whenConferenceIsNotFound_shouldThrowAnEntityNotFoundException() {
            Long conferenceId = 1L;
            UpdateConferenceRequest request = new UpdateConferenceRequest("title", null, null, null, null);

            when(repository.findById(conferenceId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.updateConference(conferenceId, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Conference with ID %d was not found.", conferenceId);

            verify(repository).findById(conferenceId);
            verifyNoMoreInteractions(repository);

        }

        @Test
        @DisplayName("when conference is found, then it should update it")
        void whenConferenceIsFound_shouldUpdateIt() {
            UpdateConferenceRequest request = new UpdateConferenceRequest("newTitle", null, null, null, null);
            Conference conference = ConferenceMock.newConferenceDomain();

            when(repository.findById(conference.getId())).thenReturn(Optional.of(conference));
            when(repository.save(request.apply(conference))).thenReturn(request.apply(conference));

            useCase.updateConference(conference.getId(), request);

            verify(repository).findById(conference.getId());
            verify(repository).save(request.apply(conference));
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: deleteConference(Long)")
    class DeleteConferenceMethod {

        @Test
        @DisplayName("when conference is not found, then it should throw an EntityNotFoundException")
        void whenConferenceIsNotFound_shouldThrowAnEntityNotFoundException() {
            when(repository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.deleteConference(1L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Conference with ID %d was not found.", 1L);

            verify(repository).findById(1L);
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("when conference has sessions, then it should throw a DeleteConflictException")
        void whenConferenceHasSessions_shouldThrowADeleteConflictException() {
            Conference conference = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .sessions(List.of(SessionMock.newSessionDomain()))
                    .build();

            when(repository.findById(conference.getId())).thenReturn(Optional.of(conference));

            assertThatThrownBy(() -> useCase.deleteConference(conference.getId()))
                    .isInstanceOf(DeletionConflictException.class)
                    .hasMessage("Could not delete Conference %d because it has associated sessions", conference.getId());

            verify(repository).findById(conference.getId());
            verifyNoMoreInteractions(repository);

        }

        @Test
        @DisplayName("when conference does not have sessions, then it should delete it")
        void whenConferenceDoesNotHaveSessions_shouldDeleteIt() {
            Conference conference = ConferenceMock.newConferenceDomain()
                    .toBuilder()
                    .sessions(null)
                    .build();

            when(repository.findById(conference.getId())).thenReturn(Optional.of(conference));

            useCase.deleteConference(conference.getId());

            verify(repository).findById(conference.getId());
            verify(repository).delete(conference.getId());
            verifyNoMoreInteractions(repository);
        }
    }
}