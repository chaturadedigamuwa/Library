package com.epiceros.library.controller;

import com.epiceros.library.dto.response.BooksByCategoryResponse;
import com.epiceros.library.dto.response.ErrorResponse;
import com.epiceros.library.dto.response.LoansByMemberIdResp;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans/v1")
public class LoanController {
    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    @Autowired
    private LoanService loanService;

    /**
     * Retrieve the loans associated with the given memberId
     * @param memberId
     * @return
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<LoansByMemberIdResp> getLoansByMemberId(@PathVariable Long memberId) {
        try {
            logger.info("Received request to fetch loans for Member ID: {}", memberId);
            String message = "Borrowed books fetched successfully.";
            List<Loan> loans = loanService.getLoansByMemberId(memberId);
            LoansByMemberIdResp response = new LoansByMemberIdResp(loans, message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Internal Error: {}", e.getMessage());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ErrorResponse error = new ErrorResponse(status.value(), "Internal Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoansByMemberIdResp(error));
        }
    }
}

