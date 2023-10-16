package com.epiceros.library.strategy;

import com.epiceros.library.entity.Member;

public interface BorrowingStrategy {
    void validateBorrowing(Member member);

}
