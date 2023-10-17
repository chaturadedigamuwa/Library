package com.epiceros.library.service.impl;

import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanDao loanDao;

    @Override
    public List<Loan> getLoansByMemberId(Long memberId) {
        return loanDao.getLoansByMemberId(memberId);
    }
}

