package com.example.edugo.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
    
    public ResourceAlreadyExistsException(String resource, String value) {
        super(String.format("%s avec la valeur '%s' existe déjà", resource, value));
    }
}

