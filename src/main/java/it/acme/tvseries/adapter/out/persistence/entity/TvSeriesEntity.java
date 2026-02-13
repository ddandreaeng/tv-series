package it.acme.tvseries.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "tv_series",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_tv_series",
        columnNames = {"titolo", "anno", "regista"}
    ),
    indexes = {
        @Index(name = "ix_tv_series_titolo", columnList = "titolo"),
        @Index(name = "ix_tv_series_genere", columnList = "genere"),
        @Index(name = "ix_tv_series_anno", columnList = "anno")
    }
)
public class TvSeriesEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "titolo", nullable = false, length = 200)
    private String titolo;

    @Column(name = "anno", nullable = false)
    private Integer anno;

    @Column(name = "genere", nullable = false, length = 80)
    private String genere;

    @Column(name = "regista", nullable = false, length = 120)
    private String regista;

    @Column(name = "sinossi", columnDefinition = "text")
    private String sinossi;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getRegista() {
        return regista;
    }

    public void setRegista(String regista) {
        this.regista = regista;
    }

    public String getSinossi() {
        return sinossi;
    }

    public void setSinossi(String sinossi) {
        this.sinossi = sinossi;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
