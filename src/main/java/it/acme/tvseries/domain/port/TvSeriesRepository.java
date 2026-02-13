package it.acme.tvseries.domain.port;

import it.acme.tvseries.domain.model.TvSeries;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TvSeriesRepository {
    TvSeries save(TvSeries tvSeries);
    
    Optional<TvSeries> findById(UUID id);
    
    List<TvSeries> findAll();
    
    PageResult<TvSeries> findAll(TvSeriesFilter filter, PageRequest pageRequest);
    
    void deleteById(UUID id);
    
    boolean existsById(UUID id);
}
