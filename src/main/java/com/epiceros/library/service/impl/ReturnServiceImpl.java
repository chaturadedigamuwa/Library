package com.epiceros.library.service.impl;

import com.epiceros.library.dao.BookDao;
import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.exception.InvalidReturnRequestException;
import com.epiceros.library.service.FineService;
import com.epiceros.library.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class ReturnServiceImpl implements ReturnService {
    @Autowired
    private LoanDao loanDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private FineService fineService;


    @Override
    public List<Long> returnBooks(ReturnRequest request) throws InvalidReturnRequestException, SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            // Step 1: Validate Return Request
            validateReturnRequest(request);

            // Step 2: Mark Book as Returned
            connection.setSavepoint();
            request.getLoanIds().stream().forEach(l -> loanDao.markBookAsReturned(l));

            // Step 3: Update Copies Owned
            connection.setSavepoint();
            request.getBookIds().stream().forEach(b -> bookDao.incrementCopiesOwned(b));

            List<Long> overdueBookIds = fineService.calculateOverdueBooks(request);
            fineService.processFinesForOverdueBooks(connection, overdueBookIds);

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException("SQL Error occurred while processing the return request.", e);

        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return request.getBookIds();
    }

    @Override
    public void validateReturnRequest(ReturnRequest returnRequest) throws InvalidReturnRequestException {
        List<Long> loanIds = returnRequest.getLoanIds();
        Long memberId = returnRequest.getMemberId();
        List<Long> bookIds = returnRequest.getBookIds();

        for (Long loanId : loanIds) {
            Loan loan = loanDao.getLoanById(loanId);
            if (loan == null) {
                throw new InvalidReturnRequestException("Loan with ID " + loanId + " not found.");
            }
        }
        for (Long bookId : bookIds) {
            if (!loanDao.isBookOnLoanToMember(bookId, memberId)) {
                throw new InvalidReturnRequestException("Book with ID " + bookId + " is not currently on loan to member with ID " + memberId);
            }
        }
    }
}
