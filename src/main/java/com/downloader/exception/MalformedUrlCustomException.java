package com.downloader.exception;

public class MalformedUrlCustomException extends RuntimeException {
    public MalformedUrlCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
