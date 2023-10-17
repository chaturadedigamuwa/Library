package com.epiceros.library.exception;

public class BorrowingNotAllowedException extends RuntimeException {
    public BorrowingNotAllowedException(String message) {
        super(message);
    }
}
