package com.epiceros.library.service;

import com.epiceros.library.dto.request.ReturnRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface FineService {
    List<Long> calculateOverdueBooks(ReturnRequest request);
    void processFinesForOverdueBooks(Connection connection, List<Long> overdueBookIds) throws SQLException;
}

