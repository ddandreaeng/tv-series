package it.acme.tvseries.application;

import it.acme.tvseries.application.usecase.GetAllTvSeriesUseCase;
import it.acme.tvseries.domain.model.TvSeries;
import it.acme.tvseries.domain.port.PageRequest;
import it.acme.tvseries.domain.port.PageResult;
import it.acme.tvseries.domain.port.TvSeriesFilter;
import it.acme.tvseries.domain.port.TvSeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetAllTvSeriesUseCaseTest {

    private TvSeriesRepository repository;
    private GetAllTvSeriesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(TvSeriesRepository.class);
        useCase = new GetAllTvSeriesUseCase(repository);
    }

    @Test
    void shouldGetAllTvSeries() {
        List<TvSeries> expected = List.of(
            new TvSeries(UUID.randomUUID(), "Breaking Bad", 2008, "Drama", "Vince Gilligan", "Synopsis 1"),
            new TvSeries(UUID.randomUUID(), "The Wire", 2002, "Crime", "David Simon", "Synopsis 2")
        );
        
        when(repository.findAll()).thenReturn(expected);

        List<TvSeries> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals(expected, result);
        verify(repository).findAll();
    }

    @Test
    void shouldGetAllWithFilterAndPagination() {
        TvSeriesFilter filter = new TvSeriesFilter("Breaking", "Drama", 2000, 2010);
        PageRequest pageRequest = PageRequest.of(0, 10, "titolo");
        
        List<TvSeries> items = List.of(
            new TvSeries(UUID.randomUUID(), "Breaking Bad", 2008, "Drama", "Vince Gilligan", "Synopsis")
        );
        PageResult<TvSeries> expected = new PageResult<>(items, 0, 10, 1);
        
        when(repository.findAll(any(TvSeriesFilter.class), any(PageRequest.class))).thenReturn(expected);

        PageResult<TvSeries> result = useCase.execute(filter, pageRequest);

        assertEquals(1, result.items().size());
        assertEquals(1, result.total());
        assertEquals(0, result.page());
        verify(repository).findAll(any(TvSeriesFilter.class), any(PageRequest.class));
    }
}
