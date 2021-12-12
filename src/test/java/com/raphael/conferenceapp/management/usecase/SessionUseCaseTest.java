package com.raphael.conferenceapp.management.usecase;

import com.raphael.conferenceapp.management.entity.Session;
import com.raphael.conferenceapp.management.exception.EntityNotFoundException;
import com.raphael.conferenceapp.management.mock.SessionMock;
import com.raphael.conferenceapp.management.usecase.request.CreateSessionRequest;
import com.raphael.conferenceapp.management.usecase.request.UpdateSessionRequest;
import com.raphael.conferenceapp.management.usecase.response.SessionResponse;
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
class SessionUseCaseTest {

    @InjectMocks
    private SessionUseCase useCase;

    @Mock
    private SessionRepository repository;

    @Nested
    @DisplayName("method: getSessionsByConferenceId(Long)")
    class GetSessionByConferenceIdMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the underlying repository")
        void whenCalled_shouldForwardTheCallToTheUnderlyingRepository() {
            List<Session> sessions = List.of(SessionMock.newSessionDomain(), SessionMock.newSessionDomain());
            when(repository.findByConferenceId(2L)).thenReturn(sessions);

            assertThat(useCase.getSessionsByConferenceId(2L))
                    .containsExactly(SessionResponse.fromDomain(sessions.get(0)), SessionResponse.fromDomain(sessions.get(1)));

            verify(repository).findByConferenceId(2L);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: createSession(CreateSessionRequest)")
    class CreateSessionRequestMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the underlying repository")
        void whenCalled_shouldForwardTheCallToTheUnderlyingRepository() {
            CreateSessionRequest request = SessionMock.newCreateRequest();
            Session session = request.toDomain();

            when(repository.save(session)).thenReturn(session);

            SessionResponse expected = SessionResponse.fromDomain(session);
            SessionResponse actual = useCase.createSession(request);

            assertThat(actual).isEqualTo(expected);

            verify(repository).save(session);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: updateSession(Long, UpdateSessionRequest)")
    class UpdateSessionMethod {

        @Test
        @DisplayName("when session is not found, then it should throw an exception")
        void whenSessionIsNotFound_shouldThrowAnException() {
            when(repository.findById(2L)).thenReturn(Optional.empty());

            UpdateSessionRequest request = new UpdateSessionRequest("title", null, null, null, null, null);

            assertThatThrownBy(() -> useCase.updateSession(2L, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Session with ID %d not found.", 2L);

            verify(repository).findById(2L);
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("when session is found, then it should update it")
        void whenSessionIsFound_shouldUpdateIt() {
            Session session = SessionMock.newSessionDomain();
            when(repository.findById(session.getId())).thenReturn(Optional.of(session));

            UpdateSessionRequest request = new UpdateSessionRequest("title", null, null, null, null, null);

            useCase.updateSession(session.getId(), request);

            verify(repository).findById(session.getId());
            verify(repository).save(request.apply(session));
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: delete(Long)")
    class DeleteMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the underlying repository")
        void whenCalled_shouldForwardTheCallToTheUnderlyingRepository() {
            useCase.deleteSession(1L);

            verify(repository).delete(1L);
            verifyNoMoreInteractions(repository);
        }
    }
}