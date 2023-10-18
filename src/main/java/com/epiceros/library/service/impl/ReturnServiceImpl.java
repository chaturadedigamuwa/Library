package com.epiceros.library.service.impl;

import com.epiceros.library.dao.BookDao;
import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.exception.InvalidReturnRequestException;
import com.epiceros.library.service.FineService;
import com.epiceros.library.service.ReturnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class ReturnServiceImpl implements ReturnService {

    private static final Logger logger = LoggerFactory.getLogger(ReturnServiceImpl.class);

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
            logger.info("Starting return process for Request: {}", request);

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

            logger.info("Return process completed successfully for Request: {}", request);

        } catch (SQLException e) {
            connection.rollback();

            logger.error("SQL Error occurred while processing the return request: {}", e.getMessage());

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
                logger.error("Loan with ID {} not found.", loanId);
                throw new InvalidReturnRequestException("Loan with ID " + loanId + " not found.");
            }
        }
        for (Long bookId : bookIds) {
            if (!loanDao.isBookOnLoanToMember(bookId, memberId)) {
                logger.error("Book with ID {} is not currently on loan to member with ID {}", bookId, memberId);
                throw new InvalidReturnRequestException("Book with ID " + bookId + " is not currently on loan to member with ID " + memberId);
            }
        }
    }
}
