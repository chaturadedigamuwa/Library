package com.epiceros.library.dao;

import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.Loan;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface LoanDao {

    void saveLoan(long memberId, long bookId) throws SQLException;
    public List<Book> getLoanedBooksByMemberId(Long memberId);

    boolean isBookOnLoanToMember(Long bookId, Long memberId);

    void markBookAsReturned(Long loanId);

    Loan getLoanById(Long loanId);

    LocalDate getDueDateForBook(Long bookId);

    List<Long> getOverdueBookIds();
}
