package com.epiceros.library.entity;

import java.time.LocalDate;
import java.util.Date;

public class Loan {
    private Long id;
    private Long memberId;
    private Long bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;

    public Loan() {

    }
    public Loan(Long id, Long memberId, Long bookId, LocalDate loanDate, LocalDate dueDate) {
        this.id = id;
        this.memberId = memberId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public Loan(Long id, Long memberId) {
        this.id = id;
        this.memberId = memberId;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
