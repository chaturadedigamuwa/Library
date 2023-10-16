package com.epiceros.library.entity;

import java.time.LocalDate;

public class Book {
    private Long id;
    private String title;
    private BookCategory category;
    private LocalDate publicationDate;
    private int copiesOwned;

    public Book(Long id, String title, BookCategory category, LocalDate publicationDate, int copiesOwned) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.publicationDate = publicationDate;
        this.copiesOwned = copiesOwned;
    }

    public Book() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getCopiesOwned() {
        return copiesOwned;
    }

    public void setCopiesOwned(int copiesOwned) {
        this.copiesOwned = copiesOwned;
    }

}

