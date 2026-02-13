package it.acme.tvseries.domain.port;

public record TvSeriesFilter(
    String query,
    String genere,
    Integer annoFrom,
    Integer annoTo
) {
    public static TvSeriesFilter empty() {
        return new TvSeriesFilter(null, null, null, null);
    }
}
