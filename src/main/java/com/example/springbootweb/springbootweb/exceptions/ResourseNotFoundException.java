package com.example.springbootweb.springbootweb.exceptions;

public class ResourseNotFoundException extends RuntimeException {


    public ResourseNotFoundException(String message) {
        super(message); // taaki parent class RuntimeException ke constructor ko iss msg ke saath invoke kr skey
    }
}
