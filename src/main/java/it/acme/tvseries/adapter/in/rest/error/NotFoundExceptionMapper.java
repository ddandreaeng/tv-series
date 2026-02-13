package it.acme.tvseries.adapter.in.rest.error;

import it.acme.tvseries.domain.error.TvSeriesNotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<TvSeriesNotFoundException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(TvSeriesNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.builder()
            .type("https://example.com/problems/not-found")
            .title("Resource not found")
            .status(404)
            .detail(exception.getMessage())
            .instance(uriInfo.getPath())
            .errorCode("NOT_FOUND")
            .build();

        return Response.status(404)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }
}
