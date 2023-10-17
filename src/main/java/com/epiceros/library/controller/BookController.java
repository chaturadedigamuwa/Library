package com.epiceros.library.controller;

import com.epiceros.library.dto.request.BorrowRequest;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.dto.response.BorrowResponse;
import com.epiceros.library.dto.response.ErrorResponse;
import com.epiceros.library.dto.response.ReturnResponse;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.exception.BookNotFoundException;
import com.epiceros.library.exception.BorrowingLimitExceededException;
import com.epiceros.library.exception.InvalidReturnRequestException;
import com.epiceros.library.exception.MemberNotFoundException;
import com.epiceros.library.service.BorrowService;
import com.epiceros.library.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("books/api/v1")
public class BookController {

    @Autowired
    private BorrowService borrowService;
    @Autowired
    private ReturnService returnService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowResponse> borrowBooks(@RequestBody BorrowRequest request) {
        try {
            List<Loan> borrowedBooks = borrowService.borrowBooks(request);
            String message = "Books borrowed successfully.";
            BorrowResponse response = new BorrowResponse(borrowedBooks, message);
            return ResponseEntity.ok(response);
        } catch (BorrowingLimitExceededException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            ErrorResponse error = new ErrorResponse(status.value(), "Borrowing limit exceeded: " + e.getMessage());
            return ResponseEntity.status(status).body(new BorrowResponse(error));
        } catch (MemberNotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ErrorResponse error = new ErrorResponse(status.value(), "Not Found Exception " + e.getMessage());
            return ResponseEntity.status(status).body(new BorrowResponse(error));
        } catch (BookNotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ErrorResponse error = new ErrorResponse(status.value(), "Not Found Exception " + e.getMessage());
            return ResponseEntity.status(status).body(new BorrowResponse(error));
        } catch (Exception e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ErrorResponse error = new ErrorResponse((status).value(), "An error occurred while borrowing the book." + e.getMessage());
            return ResponseEntity.status(status).body(new BorrowResponse(error));
        }
    }

    @PostMapping("/return")
    public ResponseEntity<ReturnResponse> returnBooks(@RequestBody ReturnRequest request) {
        try {
            List<Long> returnedBookIds = returnService.returnBooks(request);
            String message = "Books returned successfully.";
            ReturnResponse response = new ReturnResponse(returnedBookIds, message);
            return ResponseEntity.ok(response);
        } catch (InvalidReturnRequestException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            ErrorResponse error = new ErrorResponse(status.value(), "Invalid Return Request: " + e.getMessage());
            return ResponseEntity.status(status).body(new ReturnResponse(error));
        } catch (SQLException e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ErrorResponse error = new ErrorResponse(status.value(), "Internal Error: " + e.getMessage());
            return ResponseEntity.status(status).body(new ReturnResponse(error));
        } catch (Exception e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ErrorResponse error = new ErrorResponse(status.value(), "Internal Error: " + e.getMessage());
            return ResponseEntity.status(status).body(new ReturnResponse(error));
        }

    }
}
