package org.example.qrcodeservice.exception;

public class WrongImageTypeException extends RuntimeException {

    public WrongImageTypeException(String message) {
        super(message);
    }
}