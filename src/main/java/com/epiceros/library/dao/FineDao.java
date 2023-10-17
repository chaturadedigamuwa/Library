package com.epiceros.library.dao;

public interface FineDao {
    void saveFine(Long bookId, double fineAmount);
}
