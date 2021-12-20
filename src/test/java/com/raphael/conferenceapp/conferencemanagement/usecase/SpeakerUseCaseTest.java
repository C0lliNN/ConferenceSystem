package com.raphael.conferenceapp.conferencemanagement.usecase;

import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;
import com.raphael.conferenceapp.conferencemanagement.exception.EntityNotFoundException;
import com.raphael.conferenceapp.conferencemanagement.mock.SpeakerMock;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.CreateSpeakerRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.SearchSpeakersRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.request.UpdateSpeakerRequest;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.PaginatedItemsResponse;
import com.raphael.conferenceapp.conferencemanagement.usecase.response.SpeakerResponse;
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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpeakerUseCaseTest {

    @InjectMocks
    private SpeakerUseCase useCase;

    @Mock
    private SpeakerRepository repository;

    @Nested
    @DisplayName("method: getSpeakers(SearchSpeakersRequest)")
    class GetSpeakersMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the underlying repository")
        void whenCalled_shouldForwardTheCallToTheUnderlyingRepository() {
            SearchSpeakersRequest request = new SearchSpeakersRequest(null, null, null, null);

            Speaker speaker1 = SpeakerMock.newSpeakerDomain();
            Speaker speaker2 = SpeakerMock.newSpeakerDomain();

            PaginatedItems<Speaker> paginatedItems = new PaginatedItems<>(
                    List.of(speaker1, speaker2),
                    10L,
                    2L,
                    0L
            );

            when(repository.findByQuery(request.toQuery())).thenReturn(paginatedItems);

            PaginatedItemsResponse<SpeakerResponse> expected = PaginatedItemsResponse.fromPaginatedSpeakers(paginatedItems);
            PaginatedItemsResponse<SpeakerResponse> actual = useCase.getSpeakers(request);

            assertThat(actual).isEqualTo(expected);

            verify(repository).findByQuery(request.toQuery());
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: getSpeaker(Long)")
    class GetSpeakerMethod {

        @Test
        @DisplayName("when speaker is not found, then it should throw an exception")
        void whenSpeakerIsNotFound_shouldThrowAnException() {
            Long speakerId = 1L;
            when(repository.findById(speakerId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.getSpeaker(speakerId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Speaker with ID %d not found.", speakerId);

            verify(repository).findById(speakerId);
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("when speaker is found, then it should return it")
        void whenSpeakerIsFound_shouldReturnIt() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();
            when(repository.findById(speaker.getId())).thenReturn(Optional.of(speaker));

            SpeakerResponse expected = SpeakerResponse.fromDomain(speaker);
            SpeakerResponse actual = useCase.getSpeaker(speaker.getId());

            assertThat(actual).isEqualTo(expected);

            verify(repository).findById(speaker.getId());
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: createSpeaker(CreateSpeakerRequest)")
    class CreateSpeakerMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the repository")
        void whenCalled_shouldForwardTheCallToTheUnderlyingRepository() {
            CreateSpeakerRequest request = SpeakerMock.newCreateRequest();
            Speaker speaker = request.toDomain();

            when(repository.save(speaker)).thenReturn(speaker);

            SpeakerResponse expected = SpeakerResponse.fromDomain(speaker);
            SpeakerResponse actual = useCase.createSpeaker(request);

            assertThat(actual).isEqualTo(expected);

            verify(repository).save(speaker);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: updateSpeaker(Long, UpdateSpeakerRequest)")
    class UpdateSpeakerMethod {

        @Test
        @DisplayName("when speaker is not found, then it should throw an exception")
        void whenSpeakerIsNotFound_shouldThrowAnException() {
            Long speakerId = 1L;
            UpdateSpeakerRequest request = new UpdateSpeakerRequest("new name", null, null, null);

            when(repository.findById(speakerId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.updateSpeaker(speakerId, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Speaker with ID %d not found.", speakerId);

            verify(repository).findById(speakerId);
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("when speaker is found, then it should update it")
        void whenSpeakerIsFound_shouldUpdateIt() {
            Speaker speaker = SpeakerMock.newSpeakerDomain();
            UpdateSpeakerRequest request = new UpdateSpeakerRequest("new name", null, null, null);

            when(repository.findById(speaker.getId())).thenReturn(Optional.of(speaker));
            when(repository.save(request.apply(speaker))).thenReturn(request.apply(speaker));

            assertThatCode(() -> useCase.updateSpeaker(speaker.getId(), request))
                    .doesNotThrowAnyException();


            verify(repository).findById(speaker.getId());
            verify(repository).save(request.apply(speaker));
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("method: delete(Long)")
    class DeleteMethod {

        @Test
        @DisplayName("when called, then it should forward the call to the repository")
        void whenCalled_shouldForwardTheCallToTheRepository() {
            Long speakerId = 1L;

            useCase.deleteSpeaker(speakerId);

            verify(repository).delete(speakerId);
            verifyNoMoreInteractions(repository);
        }
    }
}