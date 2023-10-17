package com.epiceros.library.strategy.impl;

import com.epiceros.library.entity.Member;
import com.epiceros.library.strategy.BorrowingStrategy;
import com.epiceros.library.strategy.impl.validation.MaxTotalBorrowedStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StandardBookBorrowingStrategy implements BorrowingStrategy {
    @Autowired
    private MaxTotalBorrowedStrategy totalBorrowedStrategy;

    @Override
    public void validateBorrowing(Member member) {
        totalBorrowedStrategy.validateBorrowing(member);
    }
}
