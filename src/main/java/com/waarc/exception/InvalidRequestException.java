package com.waarc.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(Exception message) {
        super(message);
    }

    public InvalidRequestException(String invalidFileType) {
    }
}
