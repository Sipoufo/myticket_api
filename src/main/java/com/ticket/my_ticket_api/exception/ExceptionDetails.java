package com.ticket.my_ticket_api.exception;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDetails {
    protected String title;
    protected String details;
    protected String developerMessage;
    protected int status;
    protected String timestamp;

    public static ExceptionDetails createExceptionDetails(Exception ex, HttpStatusCode statusCode) {
        return createExceptionDetails(ex, statusCode, "Internal Error.");
    }

    public static ExceptionDetails createExceptionDetails(Exception ex, HttpStatusCode statusCode, String exTitle) {
        Throwable cause = ex.getCause();

        String title = cause != null
                ? cause.getMessage()
                : exTitle;

        return ExceptionDetails
                .builder()
                .status(statusCode.value())
                .title(title)
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
