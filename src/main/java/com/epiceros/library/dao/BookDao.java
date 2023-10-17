package com.epiceros.library.dao;

import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Book;

public interface BookDao {
    Book getBookById(Long bookId);
    void incrementCopiesOwned(Long bookId);
    void decrementCopiesOwned(Long bookId);

}
