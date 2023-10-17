package com.epiceros.library.dto;

import com.epiceros.library.entity.Author;
import com.epiceros.library.entity.BookCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BookDto {
    private String title;
    private BookCategory category;
    private LocalDate publicationDate;
    private int copiesOwned;
}
