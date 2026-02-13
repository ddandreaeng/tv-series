package it.acme.tvseries.application;

import it.acme.tvseries.application.usecase.GetTvSeriesByIdUseCase;
import it.acme.tvseries.domain.error.TvSeriesNotFoundException;
import it.acme.tvseries.domain.model.TvSeries;
import it.acme.tvseries.domain.port.TvSeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTvSeriesByIdUseCaseTest {

    private TvSeriesRepository repository;
    private GetTvSeriesByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(TvSeriesRepository.class);
        useCase = new GetTvSeriesByIdUseCase(repository);
    }

    @Test
    void shouldGetTvSeriesById() {
        UUID id = UUID.randomUUID();
        TvSeries expected = new TvSeries(id, "Breaking Bad", 2008, "Drama", "Vince Gilligan", "Synopsis");
        
        when(repository.findById(id)).thenReturn(Optional.of(expected));

        TvSeries result = useCase.execute(id);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(repository).findById(id);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotExists() {
        UUID id = UUID.randomUUID();
        
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TvSeriesNotFoundException.class, () -> useCase.execute(id));
        verify(repository).findById(id);
    }
}
