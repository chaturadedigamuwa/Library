package com.epiceros.library.dto.response;

import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.Loan;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BooksByCategoryResponse {
    private List<Book> books;
    private String message;
    private ErrorResponse error;

    public BooksByCategoryResponse(ErrorResponse error) {
        this.error = error;
    }
    public BooksByCategoryResponse(List<Book> bookList, String message) {
        this.books = bookList;
        this.message = message;
    }
}
