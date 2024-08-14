package org.example.qrcodeservice.exception;

public class WrongCorrectionLevelException extends RuntimeException {

    public WrongCorrectionLevelException(String message) {
        super(message);
    }
}