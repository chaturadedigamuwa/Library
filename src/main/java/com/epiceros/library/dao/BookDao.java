package com.epiceros.library.dao;

import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Author;
import com.epiceros.library.entity.Book;

import java.util.List;

public interface BookDao {
    Book getBookById(Long bookId);
    void incrementCopiesOwned(Long bookId);
    void decrementCopiesOwned(Long bookId);
    void saveBook(Book book);
    void saveAuthor(Author author);
    List<Book> getBooksByCategory(String category);
}
