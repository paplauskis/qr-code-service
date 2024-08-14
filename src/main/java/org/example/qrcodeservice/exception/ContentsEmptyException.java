package org.example.qrcodeservice.exception;

public class ContentsEmptyException extends RuntimeException {

    public ContentsEmptyException(String message) {
        super(message);
    }
}