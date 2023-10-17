package com.epiceros.library.dto.response;

import com.epiceros.library.entity.Loan;
import lombok.Data;

import java.util.List;

@Data
public class BorrowResponse {
    private List<Loan> borrowedBooks;
    private String message;
    private ErrorResponse error;


    public BorrowResponse(List<Loan> borrowedBooks, String message) {
        this.borrowedBooks = borrowedBooks;
        this.message = message;
    }

    public BorrowResponse(ErrorResponse error) {
        this.error = error;
    }
}
