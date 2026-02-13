package it.acme.tvseries.adapter.in.rest;

import it.acme.tvseries.adapter.in.rest.dto.*;
import it.acme.tvseries.adapter.in.rest.mapper.TvSeriesRestMapper;
import it.acme.tvseries.application.usecase.*;
import it.acme.tvseries.domain.port.PageRequest;
import it.acme.tvseries.domain.port.PageResult;
import it.acme.tvseries.domain.port.TvSeriesFilter;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/api/series")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "TV Series", description = "TV Series CRUD operations")
public class TvSeriesResource {

    @Inject
    CreateTvSeriesUseCase createUseCase;

    @Inject
    GetAllTvSeriesUseCase getAllUseCase;

    @Inject
    GetTvSeriesByIdUseCase getByIdUseCase;

    @Inject
    UpdateTvSeriesUseCase updateUseCase;

    @Inject
    DeleteTvSeriesUseCase deleteUseCase;

    @Inject
    TvSeriesRestMapper mapper;

    @GET
    @Operation(summary = "List TV series with optional filters and pagination")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = PagedResponse.class)))
    })
    public Response list(
            @QueryParam("q") String query,
            @QueryParam("genere") String genere,
            @QueryParam("annoFrom") Integer annoFrom,
            @QueryParam("annoTo") Integer annoTo,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sort") String sort) {

        var filter = new TvSeriesFilter(query, genere, annoFrom, annoTo);
        var pageRequest = PageRequest.of(page, size, sort);

        var result = getAllUseCase.execute(filter, pageRequest);

        var items = result.items().stream()
                .map(mapper::toListItem)
                .collect(Collectors.toList());

        var response = new PagedResponse<>(items, result.page(), result.size(), result.total());

        return Response.ok(response).build();
    }

    @POST
    @Operation(summary = "Create a new TV series")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Created",
            content = @Content(schema = @Schema(implementation = TvSeriesDto.class))),
        @APIResponse(responseCode = "400", description = "Validation error"),
        @APIResponse(responseCode = "409", description = "Duplicate entry")
    })
    public Response create(@Valid CreateTvSeriesRequest request) {
        var tvSeries = mapper.toDomain(request);
        var created = createUseCase.execute(
            tvSeries.getId(),
            tvSeries.getTitolo(),
            tvSeries.getAnno(),
            tvSeries.getGenere(),
            tvSeries.getRegista(),
            tvSeries.getSinossi()
        );

        var dto = mapper.toDto(created);
        URI location = URI.create("/api/series/" + created.getId());

        return Response.created(location).entity(dto).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get TV series by ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = TvSeriesDto.class))),
        @APIResponse(responseCode = "400", description = "Invalid UUID"),
        @APIResponse(responseCode = "404", description = "Not found")
    })
    public Response getById(@PathParam("id") String idStr) {
        UUID id = parseUUID(idStr);
        var tvSeries = getByIdUseCase.execute(id);
        var dto = mapper.toDto(tvSeries);
        return Response.ok(dto).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update TV series (replace)")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Updated",
            content = @Content(schema = @Schema(implementation = TvSeriesDto.class))),
        @APIResponse(responseCode = "400", description = "Validation error or invalid UUID"),
        @APIResponse(responseCode = "404", description = "Not found"),
        @APIResponse(responseCode = "409", description = "Duplicate entry")
    })
    public Response update(@PathParam("id") String idStr, @Valid UpdateTvSeriesRequest request) {
        UUID id = parseUUID(idStr);
        var updated = updateUseCase.execute(
            id,
            request.titolo(),
            request.anno(),
            request.genere(),
            request.regista(),
            request.sinossi()
        );

        var dto = mapper.toDto(updated);
        return Response.ok(dto).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete TV series")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Deleted"),
        @APIResponse(responseCode = "400", description = "Invalid UUID"),
        @APIResponse(responseCode = "404", description = "Not found")
    })
    public Response delete(@PathParam("id") String idStr) {
        UUID id = parseUUID(idStr);
        deleteUseCase.execute(id);
        return Response.noContent().build();
    }

    private UUID parseUUID(String idStr) {
        try {
            return UUID.fromString(idStr);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format: " + idStr);
        }
    }
}
