package com.epiceros.library.strategy.impl.validation;

import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.entity.Member;
import com.epiceros.library.exception.BorrowingLimitExceededException;
import com.epiceros.library.strategy.CommonBorrowValidationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaxTotalBorrowedStrategy implements CommonBorrowValidationStrategy {

    private static final int MAX_TOTAL_BORROWED = 7;

    @Autowired
    private LoanDao loanDao;

    @Override
    public void validateBorrowing(Member member) {
        if(loanDao.getLoanedBooksByMemberId(member.getId()).stream().count() > MAX_TOTAL_BORROWED) {
            throw new BorrowingLimitExceededException("Exceeded maximum total borrowed articles");
        }
    }
}
