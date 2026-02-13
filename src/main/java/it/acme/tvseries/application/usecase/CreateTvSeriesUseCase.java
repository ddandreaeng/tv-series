package it.acme.tvseries.application.usecase;

import it.acme.tvseries.domain.model.TvSeries;
import it.acme.tvseries.domain.port.TvSeriesRepository;

import java.util.UUID;

public class CreateTvSeriesUseCase {
    private final TvSeriesRepository repository;

    public CreateTvSeriesUseCase(TvSeriesRepository repository) {
        this.repository = repository;
    }

    public TvSeries execute(UUID id, String titolo, int anno, String genere, String regista, String sinossi) {
        TvSeries tvSeries = new TvSeries(id, titolo, anno, genere, regista, sinossi);
        return repository.save(tvSeries);
    }
}
