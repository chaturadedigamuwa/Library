package com.epiceros.library.service;

import com.epiceros.library.entity.Loan;

import java.util.List;

public interface LoanService {
    List<Loan> getLoansByMemberId(Long memberId);
}
