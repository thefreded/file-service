package com.freded.file.server;

import jakarta.ws.rs.WebApplicationException;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomWebApplicationException extends WebApplicationException {
    private String message;
    private Integer statusCode;

    public CustomWebApplicationException(String message, Integer statusCode) {
        super(message, statusCode);

    }

}
