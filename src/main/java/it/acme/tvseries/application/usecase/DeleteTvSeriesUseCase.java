package it.acme.tvseries.application.usecase;

import it.acme.tvseries.domain.error.TvSeriesNotFoundException;
import it.acme.tvseries.domain.port.TvSeriesRepository;

import java.util.UUID;

public class DeleteTvSeriesUseCase {
    private final TvSeriesRepository repository;

    public DeleteTvSeriesUseCase(TvSeriesRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID id) {
        if (!repository.existsById(id)) {
            throw new TvSeriesNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
