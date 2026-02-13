package it.acme.tvseries.application;

import it.acme.tvseries.application.usecase.UpdateTvSeriesUseCase;
import it.acme.tvseries.domain.error.TvSeriesNotFoundException;
import it.acme.tvseries.domain.model.TvSeries;
import it.acme.tvseries.domain.port.TvSeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateTvSeriesUseCaseTest {

    private TvSeriesRepository repository;
    private UpdateTvSeriesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(TvSeriesRepository.class);
        useCase = new UpdateTvSeriesUseCase(repository);
    }

    @Test
    void shouldUpdateExistingTvSeries() {
        UUID id = UUID.randomUUID();
        TvSeries updated = new TvSeries(id, "Breaking Bad", 2008, "Crime Drama", "Vince Gilligan", "Updated synopsis");
        
        when(repository.existsById(id)).thenReturn(true);
        when(repository.save(any(TvSeries.class))).thenReturn(updated);

        TvSeries result = useCase.execute(id, "Breaking Bad", 2008, "Crime Drama", "Vince Gilligan", "Updated synopsis");

        assertNotNull(result);
        assertEquals(updated, result);
        verify(repository).existsById(id);
        verify(repository).save(any(TvSeries.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentSeries() {
        UUID id = UUID.randomUUID();
        
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(TvSeriesNotFoundException.class, 
            () -> useCase.execute(id, "Breaking Bad", 2008, "Drama", "Vince Gilligan", "Synopsis")
        );
        
        verify(repository).existsById(id);
        verify(repository, never()).save(any());
    }
}
