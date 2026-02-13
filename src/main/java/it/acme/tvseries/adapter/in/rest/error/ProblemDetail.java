package it.acme.tvseries.adapter.in.rest.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetail(
    String type,
    String title,
    int status,
    String detail,
    String instance,
    String errorCode,
    Map<String, List<String>> errors
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String type;
        private String title;
        private int status;
        private String detail;
        private String instance;
        private String errorCode;
        private Map<String, List<String>> errors;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder instance(String instance) {
            this.instance = instance;
            return this;
        }

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder errors(Map<String, List<String>> errors) {
            this.errors = errors;
            return this;
        }

        public ProblemDetail build() {
            return new ProblemDetail(type, title, status, detail, instance, errorCode, errors);
        }
    }
}
