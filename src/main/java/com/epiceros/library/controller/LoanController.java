package com.epiceros.library.controller;

import com.epiceros.library.entity.Loan;
import com.epiceros.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans/v1")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Loan>> getLoansByMemberId(@PathVariable Long memberId) {
        List<Loan> loans = loanService.getLoansByMemberId(memberId);
        return ResponseEntity.ok(loans);
    }
}

