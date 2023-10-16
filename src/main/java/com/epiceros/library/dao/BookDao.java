package com.epiceros.library.dao;

import com.epiceros.library.entity.Book;

public interface BookDao {
    Book getBookById(Long bookId);
}
