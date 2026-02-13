package it.acme.tvseries.adapter.out.persistence.mapper;

import it.acme.tvseries.adapter.out.persistence.entity.TvSeriesEntity;
import it.acme.tvseries.domain.model.TvSeries;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TvSeriesEntityMapper {

    public TvSeriesEntity toEntity(TvSeries domain) {
        TvSeriesEntity entity = new TvSeriesEntity();
        entity.setId(domain.getId());
        entity.setTitolo(domain.getTitolo());
        entity.setAnno(domain.getAnno());
        entity.setGenere(domain.getGenere());
        entity.setRegista(domain.getRegista());
        entity.setSinossi(domain.getSinossi());
        return entity;
    }

    public TvSeries toDomain(TvSeriesEntity entity) {
        return new TvSeries(
            entity.getId(),
            entity.getTitolo(),
            entity.getAnno(),
            entity.getGenere(),
            entity.getRegista(),
            entity.getSinossi()
        );
    }
}
