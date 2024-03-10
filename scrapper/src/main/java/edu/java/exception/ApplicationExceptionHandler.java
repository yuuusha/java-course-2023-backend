package edu.java.exception;

import edu.java.dto.response.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ScrapperException.class)
    public ResponseEntity<ApiErrorResponse> handleScrapperException(ScrapperException ex) {
        return new ResponseEntity<>(
            new ApiErrorResponse(
                ex.getDescription(),
                String.valueOf(ex.getStatus().value()),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
            ),
            ex.getStatus()
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception ex,
        Object body,
        HttpHeaders headers,
        HttpStatusCode statusCode,
        WebRequest request
    ) {
        if (body == null) {
            return new ResponseEntity<>(
                new ApiErrorResponse(
                    "Internal Server Error",
                    String.valueOf(statusCode.value()),
                    ex.getClass().getName(),
                    ex.getMessage(),
                    null
                ),
                headers,
                statusCode
            );
        }
        return new ResponseEntity<>(body, headers, statusCode);
    }
}

