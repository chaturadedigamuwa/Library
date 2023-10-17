package com.epiceros.library.service;

import com.epiceros.library.dto.BookDto;
import com.epiceros.library.dto.request.AddBookRequest;
import com.epiceros.library.entity.Book;

import java.util.List;

public interface BookService {

    void addBook(AddBookRequest addBookRequest);

    List<Book> getBooksByCategory(String category);
}
