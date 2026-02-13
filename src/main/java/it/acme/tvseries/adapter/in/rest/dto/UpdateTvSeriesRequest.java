package it.acme.tvseries.adapter.in.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTvSeriesRequest(
    @NotBlank(message = "must not be blank")
    @Size(max = 200, message = "must be at most 200 characters")
    String titolo,
    
    @Min(value = 1900, message = "must be greater than or equal to 1900")
    @Max(value = 2100, message = "must be less than or equal to 2100")
    int anno,
    
    @NotBlank(message = "must not be blank")
    @Size(max = 80, message = "must be at most 80 characters")
    String genere,
    
    @NotBlank(message = "must not be blank")
    @Size(max = 120, message = "must be at most 120 characters")
    String regista,
    
    @Size(max = 5000, message = "must be at most 5000 characters")
    String sinossi
) {}
