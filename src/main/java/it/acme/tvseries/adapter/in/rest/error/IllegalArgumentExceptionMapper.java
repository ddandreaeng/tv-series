package it.acme.tvseries.adapter.in.rest.error;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        ProblemDetail problem = ProblemDetail.builder()
            .type("https://example.com/problems/bad-request")
            .title("Bad request")
            .status(400)
            .detail(exception.getMessage())
            .instance(uriInfo.getPath())
            .errorCode("BAD_REQUEST")
            .build();

        return Response.status(400)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }
}
