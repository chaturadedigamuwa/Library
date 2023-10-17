package com.epiceros.library.service;

import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.exception.InvalidReturnRequestException;

import java.sql.SQLException;
import java.util.List;

public interface ReturnService {

    List<Long> returnBooks(ReturnRequest request) throws InvalidReturnRequestException, SQLException;
    void validateReturnRequest(ReturnRequest returnRequest) throws InvalidReturnRequestException;

}
