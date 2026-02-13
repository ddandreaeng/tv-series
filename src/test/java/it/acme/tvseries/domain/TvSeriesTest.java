package it.acme.tvseries.domain;

import it.acme.tvseries.domain.model.TvSeries;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TvSeriesTest {

    @Test
    void shouldCreateValidTvSeries() {
        UUID id = UUID.randomUUID();
        TvSeries series = new TvSeries(
            id,
            "Breaking Bad",
            2008,
            "Drama",
            "Vince Gilligan",
            "A high school chemistry teacher turned methamphetamine producer."
        );

        assertEquals(id, series.getId());
        assertEquals("Breaking Bad", series.getTitolo());
        assertEquals(2008, series.getAnno());
        assertEquals("Drama", series.getGenere());
        assertEquals("Vince Gilligan", series.getRegista());
        assertEquals("A high school chemistry teacher turned methamphetamine producer.", series.getSinossi());
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(IllegalArgumentException.class, () -> 
            new TvSeries(null, "Breaking Bad", 2008, "Drama", "Vince Gilligan", "Synopsis")
        );
    }

    @Test
    void shouldRejectBlankTitolo() {
        assertThrows(IllegalArgumentException.class, () -> 
            new TvSeries(UUID.randomUUID(), "", 2008, "Drama", "Vince Gilligan", "Synopsis")
        );
    }

    @Test
    void shouldRejectInvalidAnno() {
        assertThrows(IllegalArgumentException.class, () -> 
            new TvSeries(UUID.randomUUID(), "Breaking Bad", 1800, "Drama", "Vince Gilligan", "Synopsis")
        );
        assertThrows(IllegalArgumentException.class, () -> 
            new TvSeries(UUID.randomUUID(), "Breaking Bad", 2150, "Drama", "Vince Gilligan", "Synopsis")
        );
    }

    @Test
    void shouldRejectBlankGenere() {
        assertThrows(IllegalArgumentException.class, () -> 
            new TvSeries(UUID.randomUUID(), "Breaking Bad", 2008, "", "Vince Gilligan", "Synopsis")
        );
    }

    @Test
    void shouldRejectBlankRegista() {
        assertThrows(IllegalArgumentException.class, () -> 
            new TvSeries(UUID.randomUUID(), "Breaking Bad", 2008, "Drama", "", "Synopsis")
        );
    }

    @Test
    void shouldAllowNullSinossi() {
        TvSeries series = new TvSeries(
            UUID.randomUUID(), 
            "Breaking Bad", 
            2008, 
            "Drama", 
            "Vince Gilligan", 
            null
        );
        assertNull(series.getSinossi());
    }
}
