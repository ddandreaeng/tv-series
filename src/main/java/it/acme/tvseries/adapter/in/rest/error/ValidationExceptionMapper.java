package it.acme.tvseries.adapter.in.rest.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, List<String>> errors = new HashMap<>();
        
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String fieldName = extractFieldName(violation.getPropertyPath().toString());
            String message = violation.getMessage();
            errors.computeIfAbsent(fieldName, k -> new java.util.ArrayList<>()).add(message);
        }

        ProblemDetail problem = ProblemDetail.builder()
            .type("https://example.com/problems/validation-error")
            .title("Validation error")
            .status(400)
            .detail("Request validation failed")
            .instance(uriInfo.getPath())
            .errorCode("VALIDATION_ERROR")
            .errors(errors)
            .build();

        return Response.status(400)
            .entity(problem)
            .type("application/problem+json")
            .build();
    }

    private String extractFieldName(String propertyPath) {
        // propertyPath format: "methodName.arg0.fieldName" or "fieldName"
        int lastDot = propertyPath.lastIndexOf('.');
        return lastDot > 0 ? propertyPath.substring(lastDot + 1) : propertyPath;
    }
}
