package com.epiceros.library.dto.request;

import com.epiceros.library.entity.Author;
import com.epiceros.library.entity.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddBookRequest {
    private Book book;
    private List<Author> authors;
}
