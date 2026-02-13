package it.acme.tvseries.adapter.in.rest.dto;

import java.util.UUID;

public record TvSeriesListItem(
    UUID id,
    String titolo,
    int anno,
    String genere
) {}
