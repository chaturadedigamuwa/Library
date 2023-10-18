package com.epiceros.library.controller;

import com.epiceros.library.dto.BookDto;
import com.epiceros.library.dto.request.AddBookRequest;
import com.epiceros.library.dto.request.BorrowRequest;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.dto.response.BooksByCategoryResponse;
import com.epiceros.library.dto.response.BorrowResponse;
import com.epiceros.library.dto.response.ErrorResponse;
import com.epiceros.library.dto.response.ReturnResponse;
import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.exception.*;
import com.epiceros.library.service.BookService;
import com.epiceros.library.service.BorrowService;
import com.epiceros.library.service.ReturnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/books/v1")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private ReturnService returnService;

    /**
     * Handles a POST request to borrow books for a member.
     *
     * @param request The BorrowRequest containing memberId and bookIds.
     * @return ResponseEntity containing the BorrowResponse with borrowed books and a message.
     */
    @PostMapping("/borrow")
    public ResponseEntity<BorrowResponse> borrowBooks(@RequestBody BorrowRequest request) {
        try {
            List<Loan> borrowedBooks = borrowService.borrowBooks(request);
            String message = "Books borrowed successfully.";
            BorrowResponse response = new BorrowResponse(borrowedBooks, message);
            return ResponseEntity.ok(response);
        } catch (BorrowingLimitExceededException e) {
            logger.error("Borrowing limit exceeded: {}", e.getMessage());
            HttpStatus status = HttpStatus.BAD_REQUEST;
            ErrorResponse error = new ErrorResponse(status.value(), "Borrowing limit exceeded: " + e.getMessage());
            return ResponseEntity.status(status).body(new BorrowResponse(error));
        } catch (MemberNotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ErrorResponse error = new ErrorResponse(status.value(), "Not Found Exception " + e.getMessage());
            return ResponseEntity.status(status).body(new BorrowResponse(error));
        } catch (BookNotFoundException e) {
            logger.error("Book not found: {}", e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            ErrorResponse error = new ErrorResponse(status.value(), "Not Found Exception " + e.getMessage());
            return ResponseEntity.status(status).body(new BorrowResponse(error));
        } catch (Exception e) {
            logger.error("An error occurred while borrowing the book: {}", e.getMessage());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ErrorResponse error = new ErrorResponse((status).value(), "An error occurred while borrowing the book." + e.getMessage());
            return ResponseEntity.status(status).body(new BorrowResponse(error));
        }
    }


    /**
     * Handles the HTTP POST request to return borrowed books.
     *
     * @param request The ReturnRequest object containing information about the books to be returned.
     * @return A ResponseEntity containing a ReturnResponse with the list of returned book IDs and a success message.
     */
    @PostMapping("/return")
    public ResponseEntity<ReturnResponse> returnBooks(@RequestBody ReturnRequest request) {
        try {
            List<Long> returnedBookIds = returnService.returnBooks(request);
            String message = "Books returned successfully.";
            ReturnResponse response = new ReturnResponse(returnedBookIds, message);
            return ResponseEntity.ok(response);
        } catch (InvalidReturnRequestException e) {
            logger.error("Invalid Return Request: {}", e.getMessage());
            HttpStatus status = HttpStatus.BAD_REQUEST;
            ErrorResponse error = new ErrorResponse(status.value(), "Invalid Return Request: " + e.getMessage());
            return ResponseEntity.status(status).body(new ReturnResponse(error));
        } catch (Exception e) {
            logger.error("Internal Error: {}", e.getMessage());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ErrorResponse error = new ErrorResponse(status.value(), "Internal Error: " + e.getMessage());
            return ResponseEntity.status(status).body(new ReturnResponse(error));
        }

    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody AddBookRequest bookRequest) {
        try {
            bookService.addBook(bookRequest);
            return ResponseEntity.ok("Book added successfully.");
        } catch (Exception e) {
            logger.error("Error adding book: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding book: " + e.getMessage());
        }
    }

    @GetMapping("/{category}")
    public ResponseEntity<BooksByCategoryResponse> getBooksByCategory(@PathVariable String category) {
        try {
            List<Book> bookList = bookService.getBooksByCategory(category);
            String message = "Books returned successfully.";
            BooksByCategoryResponse response = new BooksByCategoryResponse(bookList, message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Internal Error: {}", e.getMessage());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ErrorResponse error = new ErrorResponse(status.value(), "Internal Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BooksByCategoryResponse(error));
        }
    }
}