package com.epiceros.library.service;

import com.epiceros.library.dto.BorrowRequest;
import com.epiceros.library.entity.Loan;

import java.sql.SQLException;
import java.util.List;

public interface BookService {
    List<Loan> borrowBooks(BorrowRequest request) throws SQLException;
}
