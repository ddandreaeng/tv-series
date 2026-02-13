package it.acme.tvseries.application;

import it.acme.tvseries.application.usecase.DeleteTvSeriesUseCase;
import it.acme.tvseries.domain.error.TvSeriesNotFoundException;
import it.acme.tvseries.domain.port.TvSeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteTvSeriesUseCaseTest {

    private TvSeriesRepository repository;
    private DeleteTvSeriesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(TvSeriesRepository.class);
        useCase = new DeleteTvSeriesUseCase(repository);
    }

    @Test
    void shouldDeleteExistingTvSeries() {
        UUID id = UUID.randomUUID();
        
        when(repository.existsById(id)).thenReturn(true);

        useCase.execute(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistentSeries() {
        UUID id = UUID.randomUUID();
        
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(TvSeriesNotFoundException.class, () -> useCase.execute(id));
        
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(any());
    }
}
