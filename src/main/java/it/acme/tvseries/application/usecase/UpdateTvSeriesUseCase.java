package it.acme.tvseries.application.usecase;

import it.acme.tvseries.domain.error.TvSeriesNotFoundException;
import it.acme.tvseries.domain.model.TvSeries;
import it.acme.tvseries.domain.port.TvSeriesRepository;

import java.util.UUID;

public class UpdateTvSeriesUseCase {
    private final TvSeriesRepository repository;

    public UpdateTvSeriesUseCase(TvSeriesRepository repository) {
        this.repository = repository;
    }

    public TvSeries execute(UUID id, String titolo, int anno, String genere, String regista, String sinossi) {
        if (!repository.existsById(id)) {
            throw new TvSeriesNotFoundException(id);
        }
        
        TvSeries updated = new TvSeries(id, titolo, anno, genere, regista, sinossi);
        return repository.save(updated);
    }
}
