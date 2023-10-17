package com.epiceros.library.dto.response;

import com.epiceros.library.entity.Loan;

import java.util.List;

public class ReturnResponse {

    private List<Long> returnedBookIds;
    private String message;
    private ErrorResponse error;


    public ReturnResponse(List<Long> borrowedBooks, String message) {
        this.returnedBookIds = borrowedBooks;
        this.message = message;
    }

    public ReturnResponse(ErrorResponse error) {
        this.error = error;
    }
}
