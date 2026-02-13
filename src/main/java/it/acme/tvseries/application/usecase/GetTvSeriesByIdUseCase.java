package it.acme.tvseries.application.usecase;

import it.acme.tvseries.domain.error.TvSeriesNotFoundException;
import it.acme.tvseries.domain.model.TvSeries;
import it.acme.tvseries.domain.port.TvSeriesRepository;

import java.util.UUID;

public class GetTvSeriesByIdUseCase {
    private final TvSeriesRepository repository;

    public GetTvSeriesByIdUseCase(TvSeriesRepository repository) {
        this.repository = repository;
    }

    public TvSeries execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new TvSeriesNotFoundException(id));
    }
}
