package it.acme.tvseries.application;

import it.acme.tvseries.application.usecase.CreateTvSeriesUseCase;
import it.acme.tvseries.domain.model.TvSeries;
import it.acme.tvseries.domain.port.TvSeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateTvSeriesUseCaseTest {

    private TvSeriesRepository repository;
    private CreateTvSeriesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(TvSeriesRepository.class);
        useCase = new CreateTvSeriesUseCase(repository);
    }

    @Test
    void shouldCreateTvSeries() {
        UUID id = UUID.randomUUID();
        TvSeries expected = new TvSeries(id, "Breaking Bad", 2008, "Drama", "Vince Gilligan", "Synopsis");
        
        when(repository.save(any(TvSeries.class))).thenReturn(expected);

        TvSeries result = useCase.execute(id, "Breaking Bad", 2008, "Drama", "Vince Gilligan", "Synopsis");

        assertNotNull(result);
        assertEquals(expected, result);
        verify(repository).save(any(TvSeries.class));
    }

    @Test
    void shouldRejectInvalidInput() {
        assertThrows(IllegalArgumentException.class, 
            () -> useCase.execute(null, "Breaking Bad", 2008, "Drama", "Vince Gilligan", "Synopsis")
        );
        
        verify(repository, never()).save(any());
    }
}
