package it.acme.tvseries.adapter.in.rest.error;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GenericExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        LOG.error("Unhandled exception", exception);
        
        ProblemDetail problem = ProblemDetail.builder()
            .type("https://example.com/problems/internal-error")
            .title("Internal server error")
            .status(500)
            .detail("An unexpected error occurred")
            .instance(uriInfo.getPath())
            .errorCode("INTERNAL_ERROR")
            .build();

        return Response.status(500)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }
}
