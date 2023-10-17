package com.epiceros.library.service.impl;

import com.epiceros.library.dao.BookDao;
import com.epiceros.library.dto.BookDto;
import com.epiceros.library.dto.request.AddBookRequest;
import com.epiceros.library.entity.Author;
import com.epiceros.library.entity.Book;
import com.epiceros.library.service.BookService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookDao bookDao;
    @Override
    public void addBook(AddBookRequest request) {

        Book book = request.getBook();
        bookDao.saveBook(book);

        List<Author> authors = request.getAuthors();
        authors.forEach(bookDao::saveAuthor);
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        return bookDao.getBooksByCategory(category);
    }
}
