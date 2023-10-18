package com.epiceros.library.strategy.impl;

import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.entity.BookCategory;
import com.epiceros.library.entity.Member;
import com.epiceros.library.exception.BorrowingLimitExceededException;
import com.epiceros.library.strategy.BorrowingStrategy;
import com.epiceros.library.strategy.impl.validation.MaxTotalBorrowedStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewBookBorrowingStrategy implements BorrowingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(NewBookBorrowingStrategy.class);

    @Autowired
    private LoanDao loanDao;
    @Autowired
    private MaxTotalBorrowedStrategy totalBorrowedStrategy;

    @Override
    public void validateBorrowing(Member member) {
        try {
            totalBorrowedStrategy.validateBorrowing(member);
            if (loanDao.getLoanedBooksByMemberId(member.getId()).stream().filter(b -> b.getCategory() == BookCategory.NEW).count() >= 2) {
                throw new BorrowingLimitExceededException("You have borrowed the maximum allowed number of NEW-books.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while validating borrowing for Member ID {}: {}", member.getId(), e.getMessage());
            throw e;
        }
    }
}
