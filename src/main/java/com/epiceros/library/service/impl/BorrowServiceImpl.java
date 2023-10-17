package com.epiceros.library.service.impl;

import com.epiceros.library.dao.BookDao;
import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.dao.MemberDao;
import com.epiceros.library.dto.request.BorrowRequest;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.entity.Member;
import com.epiceros.library.exception.BookNotFoundException;
import com.epiceros.library.exception.MemberNotFoundException;
import com.epiceros.library.factory.BorrowingStrategyFactory;
import com.epiceros.library.service.BorrowService;
import com.epiceros.library.strategy.BorrowingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private LoanDao loanDao;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private BorrowingStrategyFactory strategyFactory;

    private static final int MAX_TOTAL_BORROW_ONE_TIME = 5;


    public List<Loan> borrowBooks(BorrowRequest request) throws SQLException {
        Long memberId = request.getMemberId();
        List<Long> bookIds = request.getBookIds();
        List<Loan> createdLoans = new ArrayList<>();

        // Ensure the request contains no more than 5 books
        if (bookIds.size() > MAX_TOTAL_BORROW_ONE_TIME) {
            throw new IllegalArgumentException("A maximum of 5 books can be borrowed at a time.");
        }

        Optional<Member> memberOptional = Optional.ofNullable(memberDao.getMemberById(memberId));
        memberOptional.orElseThrow(() -> new MemberNotFoundException("Member with ID " + memberId + " not found."));

        for (Long bookId : bookIds) {
            Optional<Book> bookOptional = Optional.ofNullable(bookDao.getBookById(bookId));
            bookOptional.orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found."));

            BorrowingStrategy strategy = strategyFactory.getStrategy(bookOptional.get().getCategory());
            strategy.validateBorrowing(memberOptional.get());

            loanDao.saveLoan(memberId, bookId);
            bookDao.decrementCopiesOwned(bookId);

            Loan loan = new Loan();
            loan.setMemberId(memberId);
            loan.setBookId(bookId);
            createdLoans.add(loan);
        }
        return createdLoans;
    }
}

