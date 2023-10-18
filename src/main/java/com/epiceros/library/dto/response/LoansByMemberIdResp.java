package com.epiceros.library.dto.response;

import com.epiceros.library.entity.Loan;
import lombok.Data;

import java.util.List;

@Data
public class LoansByMemberIdResp {
    private List<Loan> borrowedBooks;
    private String message;
    private ErrorResponse error;

    public LoansByMemberIdResp(List<Loan> borrowedBooks, String message) {
        this.borrowedBooks = borrowedBooks;
        this.message = message;
    }

    public LoansByMemberIdResp(ErrorResponse error) {
        this.error = error;
    }

}
