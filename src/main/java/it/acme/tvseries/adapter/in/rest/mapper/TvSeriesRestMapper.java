package it.acme.tvseries.adapter.in.rest.mapper;

import it.acme.tvseries.adapter.in.rest.dto.CreateTvSeriesRequest;
import it.acme.tvseries.adapter.in.rest.dto.TvSeriesDto;
import it.acme.tvseries.adapter.in.rest.dto.TvSeriesListItem;
import it.acme.tvseries.adapter.in.rest.dto.UpdateTvSeriesRequest;
import it.acme.tvseries.domain.model.TvSeries;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class TvSeriesRestMapper {

    public TvSeries toDomain(CreateTvSeriesRequest request) {
        return new TvSeries(
            UUID.randomUUID(),
            request.titolo(),
            request.anno(),
            request.genere(),
            request.regista(),
            request.sinossi()
        );
    }

    public TvSeries toDomain(UUID id, UpdateTvSeriesRequest request) {
        return new TvSeries(
            id,
            request.titolo(),
            request.anno(),
            request.genere(),
            request.regista(),
            request.sinossi()
        );
    }

    public TvSeriesDto toDto(TvSeries tvSeries) {
        return new TvSeriesDto(
            tvSeries.getId(),
            tvSeries.getTitolo(),
            tvSeries.getAnno(),
            tvSeries.getGenere(),
            tvSeries.getRegista(),
            tvSeries.getSinossi()
        );
    }

    public TvSeriesListItem toListItem(TvSeries tvSeries) {
        return new TvSeriesListItem(
            tvSeries.getId(),
            tvSeries.getTitolo(),
            tvSeries.getAnno(),
            tvSeries.getGenere()
        );
    }
}
