package com.example.springbootweb.springbootweb.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data   // so that we can use getters and setters everywhere
@Builder // Builder pattern
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errorList;
}
