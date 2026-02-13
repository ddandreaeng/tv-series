package it.acme.tvseries.application.usecase;

import it.acme.tvseries.domain.model.TvSeries;
import it.acme.tvseries.domain.port.PageRequest;
import it.acme.tvseries.domain.port.PageResult;
import it.acme.tvseries.domain.port.TvSeriesFilter;
import it.acme.tvseries.domain.port.TvSeriesRepository;

import java.util.List;

public class GetAllTvSeriesUseCase {
    private final TvSeriesRepository repository;

    public GetAllTvSeriesUseCase(TvSeriesRepository repository) {
        this.repository = repository;
    }

    public List<TvSeries> execute() {
        return repository.findAll();
    }
    
    public PageResult<TvSeries> execute(TvSeriesFilter filter, PageRequest pageRequest) {
        return repository.findAll(filter, pageRequest);
    }
}
