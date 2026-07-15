package com.downloader.exception;

// CHECKED exception: extends Exception, so callers must handle or declare it.
public class DownloadException extends Exception {
    public DownloadException(String message) { super(message); }
}
