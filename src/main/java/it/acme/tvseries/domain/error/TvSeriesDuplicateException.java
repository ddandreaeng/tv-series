package it.acme.tvseries.domain.error;

public class TvSeriesDuplicateException extends RuntimeException {
    public TvSeriesDuplicateException(String message) {
        super(message);
    }

    public TvSeriesDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
