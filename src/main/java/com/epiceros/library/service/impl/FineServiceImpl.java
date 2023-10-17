package com.epiceros.library.service.impl;

import com.epiceros.library.dao.BookDao;
import com.epiceros.library.dao.FineDao;
import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.Loan;
import com.epiceros.library.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FineServiceImpl implements FineService {

    @Autowired
    private LoanDao loanDao;
    @Autowired
    private BookDao bookDao;

    @Autowired
    private FineDao fineDao;
    @Override
    public List<Long> calculateOverdueBooks(ReturnRequest request) {
        List<Long> overdueBookIds = new ArrayList<>();

        for (Long loanId : request.getLoanIds()) {
            Loan loan = loanDao.getLoanById(loanId);
            if (loan != null) {
                LocalDate dueDate = loan.getDueDate();
                LocalDate today = LocalDate.now();

                if (today.isAfter(dueDate)) {
                    overdueBookIds.add(loan.getBookId());
                }
            }
        }

        return overdueBookIds;
    }

    @Override
    public void processFinesForOverdueBooks(Connection connection, List<Long> overdueBookIds) throws SQLException {
        try {
            connection.setAutoCommit(false);

            for (Long bookId : overdueBookIds) {
                Book book = bookDao.getBookById(bookId);

                double fineAmount = calculateFineForBook(book);

                // Save the fine to the database
                fineDao.saveFine(bookId, fineAmount);

            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                // Handle rollback exception
            }
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
