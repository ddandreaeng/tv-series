package it.acme.tvseries.domain.error;

import java.util.UUID;

public class TvSeriesNotFoundException extends RuntimeException {
    private final UUID id;

    public TvSeriesNotFoundException(UUID id) {
        super("TV Series not found with id: " + id);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
