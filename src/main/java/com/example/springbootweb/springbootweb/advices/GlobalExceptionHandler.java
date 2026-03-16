package com.example.springbootweb.springbootweb.advices;

import com.example.springbootweb.springbootweb.exceptions.ResourceAlreadyExistsException;
import com.example.springbootweb.springbootweb.exceptions.ResourseNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// @RestControllerAdvice -->all controllers exceptions can register in this
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(new ApiResponse<>(apiError),apiError.getStatus());
    }

    // we can define  all types of exceptions here(even custom)

    // ResourseNotFoundException is a custom exception here
    @ExceptionHandler(ResourseNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> resourceNotFoundHandler(ResourseNotFoundException exception){
//        return new ResponseEntity<>("no such resource  Found",HttpStatus.NOT_FOUND);
        ApiError apiError=ApiError
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();
//        return  new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
        return  buildErrorResponseEntity(apiError);

    }

    // ResourceAlreadyExistsException is a custom exception here
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> resourceAlreadyExistsHandler(ResourceAlreadyExistsException exception){
//
        ApiError apiError=ApiError
                .builder()
                .status(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .build();
//        return  new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
        return  buildErrorResponseEntity(apiError);

    }



    //Exception is parent/papa of all exceptions ,handle all types of app (sprinboot) exceptions here gracefully so our app/server never fatega kabhi
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerErrors(Exception exception){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
//        return new ResponseEntity<>(apiError,HttpStatus.INTERNAL_SERVER_ERROR);
        return buildErrorResponseEntity(apiError);

    }

    //Exception handling for Input payload validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleInputPayloadNotValid(MethodArgumentNotValidException exception){
        List<String> errorsList= exception.getBindingResult()
                .getAllErrors()
                .stream().map((error)->error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError =ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Input Validation Failed")
                .errorList(errorsList)
                .build();
//        return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
        return buildErrorResponseEntity(apiError);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleJsonParseError(HttpMessageNotReadableException exception){

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Malformed JSON or Invalid Data Type")
                .build();

//        return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
        return buildErrorResponseEntity(apiError);

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException exception){
        ApiError apiError =  ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(exception.getLocalizedMessage())
                .build();

        return buildErrorResponseEntity(apiError);

    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException exception){
        ApiError apiError =  ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(exception.getLocalizedMessage())
                .build();

        return buildErrorResponseEntity(apiError);

    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException exception){
//        ApiError apiError =  ApiError.builder()
//                .status(HttpStatus.FORBIDDEN)
//                .message(exception.getLocalizedMessage())
//                .build();
//
//        return buildErrorResponseEntity(apiError);
//
//    }
}
