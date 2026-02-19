package com.example.springbootweb.springbootweb.advices;

import lombok.Data;

import java.time.LocalDateTime;

// this will be uniform/constant/same format ApiResponse of every api in the app
@Data
public class ApiResponse<T> {  //generics <T> ,ie. of any type

    private LocalDateTime timeStamp;
    private T data;
    private ApiError error;

    public ApiResponse(){ // default constructor
        this.timeStamp =LocalDateTime.now();
    }
    public ApiResponse(T data){ //
        this(); // calling default constructor here also so to set timeStamp
        this.data =data;
    }
    public ApiResponse(ApiError error){ //
        this(); // calling default constructor here also so to set timeStamp
        this.error =error;
    }
    // Note : from above it is clear that timeStamp will always be there in apiResponse with either of  data or error has value.
}
