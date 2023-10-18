package com.epiceros.library.exception;

public class BooksNotAvailableException extends RuntimeException {
    public BooksNotAvailableException(String message) {
        super(message);
    }
}
