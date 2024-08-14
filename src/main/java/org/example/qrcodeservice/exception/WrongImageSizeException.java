package org.example.qrcodeservice.exception;

public class WrongImageSizeException extends RuntimeException {

    public WrongImageSizeException(String message) {
        super(message);
    }
}