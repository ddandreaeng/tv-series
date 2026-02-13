package it.acme.tvseries.adapter.in.rest.error;

import it.acme.tvseries.domain.error.TvSeriesDuplicateException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicateExceptionMapper implements ExceptionMapper<TvSeriesDuplicateException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(TvSeriesDuplicateException exception) {
        ProblemDetail problem = ProblemDetail.builder()
            .type("https://example.com/problems/conflict")
            .title("Resource conflict")
            .status(409)
            .detail(exception.getMessage())
            .instance(uriInfo.getPath())
            .errorCode("CONFLICT")
            .build();

        return Response.status(409)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }
}
