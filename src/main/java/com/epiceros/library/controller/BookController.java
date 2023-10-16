package com.epiceros.library.controller;

import com.epiceros.library.dto.BorrowRequest;
import com.epiceros.library.exception.BookNotFoundException;
import com.epiceros.library.exception.BorrowingLimitExceededException;
import com.epiceros.library.exception.MemberNotFoundException;
import com.epiceros.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("books/api/v1")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBooks(@RequestBody BorrowRequest request) {
        try {
            bookService.borrowBooks(request);
            return ResponseEntity.ok("Books borrowed successfully.");
        } catch (BorrowingLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Borrowing limit exceeded: " + e.getMessage());
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Exception " + e.getMessage());
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Exception " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while borrowing the book." + e.getMessage());
        }
    }

}
