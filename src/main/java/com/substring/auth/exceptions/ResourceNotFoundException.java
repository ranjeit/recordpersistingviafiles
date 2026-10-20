package com.substring.auth.exceptions;

public class ResourceNotFoundException extends Exception {

   String message;
    public ResourceNotFoundException(String email){
        super(email);
        this.message = email;
    }
}
