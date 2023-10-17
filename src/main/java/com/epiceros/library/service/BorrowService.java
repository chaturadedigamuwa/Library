package com.epiceros.library.service;

import com.epiceros.library.dto.request.BorrowRequest;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Loan;

import java.sql.SQLException;
import java.util.List;

public interface BorrowService {
    List<Loan> borrowBooks(BorrowRequest request) throws SQLException;

}
