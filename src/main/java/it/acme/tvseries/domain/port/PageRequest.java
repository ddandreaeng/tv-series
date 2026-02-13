package it.acme.tvseries.domain.port;

public record PageRequest(
    int page,
    int size,
    String sort
) {
    public PageRequest {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be non-negative");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }
    }
    
    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size, null);
    }
    
    public static PageRequest of(int page, int size, String sort) {
        return new PageRequest(page, size, sort);
    }
}
