package com.epiceros.library.service.impl;

import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    @Autowired
    private LoanDao loanDao;

    @Override
    public List<Loan> getLoansByMemberId(Long memberId) {
        try {
            logger.info("Fetching loans for Member ID: {}", memberId);
            List<Loan> loans = loanDao.getLoansByMemberId(memberId);
            logger.info("Fetched {} loans for Member ID: {}", loans.size(), memberId);
            return loans;
        } catch (Exception e) {
            logger.error("Error occurred while fetching loans for Member ID {}: {}", memberId, e.getMessage());
            throw e;
        }
    }
}

