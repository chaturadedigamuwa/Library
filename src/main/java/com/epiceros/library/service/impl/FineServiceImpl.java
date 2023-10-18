package com.epiceros.library.service.impl;

import com.epiceros.library.dao.BookDao;
import com.epiceros.library.dao.FineDao;
import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.service.FineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
@Service
public class FineServiceImpl implements FineService {

    private static final Logger logger = LoggerFactory.getLogger(FineServiceImpl.class);

    @Autowired
    private LoanDao loanDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private FineDao fineDao;

    /**
     * Calculates and returns a list of IDs for books that are overdue for return based on the provided request.
     *
     * @param request The ReturnRequest containing necessary information for calculating overdue books.
     * @return A list of Long values representing the IDs of overdue books.
     */
    @Override
    public List<Long> calculateOverdueBooks(ReturnRequest request) {
        List<Long> overdueBookIds = new ArrayList<>();
        for (Long loanId : request.getLoanIds()) {
            try {
                Loan loan = loanDao.getLoanById(loanId);
                if (loan != null) {
                    LocalDate dueDate = loan.getDueDate();
                    LocalDate today = LocalDate.now();

                    if (today.isAfter(dueDate)) {
                        overdueBookIds.add(loan.getBookId());
                    }
                }
            } catch (Exception e) {
                logger.error("Error occurred while calculating overdue books for loan ID {}: {}", loanId, e.getMessage());
            }
        }

        return overdueBookIds;
    }

    @Override
    public void processFinesForOverdueBooks(Connection connection, List<Long> overdueBookIds) throws SQLException {
        try {
            connection.setAutoCommit(false);

            for (Long bookId : overdueBookIds) {
                try {

                    Book book = bookDao.getBookById(bookId);

                    double fineAmount = calculateFineForBook(book);

                    // Save the fine to the database
                    fineDao.saveFine(bookId, fineAmount);
                } catch (Exception e) {
                    logger.error("Error occurred while processing fine for book ID {}: {}", bookId, e.getMessage());
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                logger.error("Error occurred during rollback: {}", rollbackException.getMessage());
            }
            logger.error("Error processing fines for overdue books: {}", e.getMessage());
            throw new RuntimeException("Error processing fines for overdue books.", e);
        }
    }

    private double calculateFineForBook(Book book) {
        LocalDate currentDate = LocalDate.now();

        LocalDate dueDate = loanDao.getDueDateForBook(book.getId());

        int daysOverdue = (int) ChronoUnit.DAYS.between(dueDate, currentDate);

        double fineRatePerDay = 0.50;

        double fineAmount;
        switch (book.getCategory()) {
            case NEW:
                fineAmount = daysOverdue * fineRatePerDay * 2;
                break;
            case CLASSIC:
                fineAmount = daysOverdue * fineRatePerDay * 1;
                break;
            case STANDARD:
                fineAmount = daysOverdue * fineRatePerDay * 0.5;
                break;
            default:
                fineAmount = 0;
                break;
        }

        return fineAmount;
    }

}
