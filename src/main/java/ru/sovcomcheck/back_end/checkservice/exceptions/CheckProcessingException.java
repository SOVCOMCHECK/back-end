package ru.sovcomcheck.back_end.checkservice.exceptions;

public class CheckProcessingException extends RuntimeException {
    public CheckProcessingException(String message) {
        super(message);
    }

    public CheckProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
