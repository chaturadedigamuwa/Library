package com.epiceros.library.strategy;

import com.epiceros.library.entity.Member;

public interface CommonBorrowValidationStrategy {
    void validateBorrowing(Member member);
}
