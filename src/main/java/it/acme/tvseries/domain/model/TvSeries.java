package it.acme.tvseries.domain.model;

import java.util.Objects;
import java.util.UUID;

public class TvSeries {
    private static final int MIN_ANNO = 1900;
    private static final int MAX_ANNO = 2100;

    private final UUID id;
    private final String titolo;
    private final int anno;
    private final String genere;
    private final String regista;
    private final String sinossi;

    public TvSeries(UUID id, String titolo, int anno, String genere, String regista, String sinossi) {
        validateId(id);
        validateTitolo(titolo);
        validateAnno(anno);
        validateGenere(genere);
        validateRegista(regista);

        this.id = id;
        this.titolo = titolo;
        this.anno = anno;
        this.genere = genere;
        this.regista = regista;
        this.sinossi = sinossi;
    }

    private void validateId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
    }

    private void validateTitolo(String titolo) {
        if (titolo == null || titolo.isBlank()) {
            throw new IllegalArgumentException("Titolo cannot be null or blank");
        }
    }

    private void validateAnno(int anno) {
        if (anno < MIN_ANNO || anno > MAX_ANNO) {
            throw new IllegalArgumentException("Anno must be between " + MIN_ANNO + " and " + MAX_ANNO);
        }
    }

    private void validateGenere(String genere) {
        if (genere == null || genere.isBlank()) {
            throw new IllegalArgumentException("Genere cannot be null or blank");
        }
    }

    private void validateRegista(String regista) {
        if (regista == null || regista.isBlank()) {
            throw new IllegalArgumentException("Regista cannot be null or blank");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public int getAnno() {
        return anno;
    }

    public String getGenere() {
        return genere;
    }

    public String getRegista() {
        return regista;
    }

    public String getSinossi() {
        return sinossi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TvSeries tvSeries = (TvSeries) o;
        return Objects.equals(id, tvSeries.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TvSeries{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", anno=" + anno +
                ", genere='" + genere + '\'' +
                ", regista='" + regista + '\'' +
                ", sinossi='" + sinossi + '\'' +
                '}';
    }
}
