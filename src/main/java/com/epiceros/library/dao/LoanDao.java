package com.epiceros.library.dao;

import com.epiceros.library.entity.Book;

import java.sql.SQLException;
import java.util.List;

public interface LoanDao {

    void saveLoan(long memberId, long bookId) throws SQLException;
    public List<Book> getLoanedBooksByMemberId(Long memberId);
}
