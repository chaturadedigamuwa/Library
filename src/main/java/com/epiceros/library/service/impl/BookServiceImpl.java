package com.epiceros.library.service.impl;

import com.epiceros.library.dao.BookDao;
import com.epiceros.library.dto.request.AddBookRequest;
import com.epiceros.library.entity.Author;
import com.epiceros.library.entity.Book;
import com.epiceros.library.exception.BooksNotAvailableException;
import com.epiceros.library.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookDao bookDao;
    @Override
    public void addBook(AddBookRequest request) {
        try {
            logger.info("Received request to add a new book");
            Book book = request.getBook();
            bookDao.saveBook(book);

            List<Author> authors = request.getAuthors();
            authors.forEach(bookDao::saveAuthor);
        } catch (Exception e) {
            logger.error("Error occurred while adding a new book: {}", e.getMessage());
            throw new RuntimeException("Error occurred while adding a new book", e);
        }
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        logger.info("Received request to fetch books by category: {}", category);
        List<Book> bookList = bookDao.getBooksByCategory(category);
        if (bookList.isEmpty()) {
            logger.info("No books available in category: {}", category);
            throw new BooksNotAvailableException("No Books Available");
        }
        logger.info("Found {} books in category: {}", bookList.size(), category);
        return bookList;
    }
}
