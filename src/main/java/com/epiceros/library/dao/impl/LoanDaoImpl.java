package com.epiceros.library.dao.impl;

import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.BookCategory;
import com.epiceros.library.entity.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoanDaoImpl implements LoanDao {
    @Autowired
    private DataSource dataSource;

    @Value("${library.fine.days}")
    private int fineDays;

    @Override
    public void saveLoan(long memberId, long bookId) throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO loan (book_id, member_id, loan_date, due_date, returned_date) " + "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, bookId);
                statement.setLong(2, memberId);
                statement.setDate(3, Date.valueOf(LocalDate.now()));
                statement.setDate(4, Date.valueOf(LocalDate.now().plusDays(fineDays)));
                statement.setDate(5, null); // Assuming returned_date is initially null
                statement.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException ex) {
            connection.rollback();
            throw new RuntimeException("Error saving loan ", ex);
        } finally {
            connection.close();
        }

    }
    @Override
    public List<Book> getLoanedBooksByMemberId(Long memberId) {
        List<Book> loanedBooks = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT b.* " +
                             "FROM loan l " +
                             "INNER JOIN book b ON l.book_id = b.id " +
                             "WHERE l.member_id = ? AND l.returned_date IS NULL")) {

            statement.setLong(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = mapResultSetToBook(resultSet);
                    loanedBooks.add(book);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching loaned books by member ID", e);
        }
        return loanedBooks;
    }

    @Override
    public boolean isBookOnLoanToMember(Long bookId, Long memberId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) FROM loan WHERE book_id = ? AND member_id = ? AND returned_date IS NULL";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, bookId);
                statement.setLong(2, memberId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if book is on loan to member.", e);
        }
        return false; // Default return if an exception occur
    }

    @Override
    public void markBookAsReturned(Long loanId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE loan SET returned_date = ? WHERE id = ?")) {

            statement.setDate(1, Date.valueOf(LocalDate.now()));
            statement.setLong(2, loanId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error marking book as returned.", e);
        }
    }

    @Override
    public Loan getLoanById(Long loanId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM loan WHERE id = ?")) {
            statement.setLong(1, loanId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToLoan(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching loan by ID", e);
        }
        return null; // Loan with the given ID not found
    }

    @Override
    public LocalDate getDueDateForBook(Long bookId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT due_date FROM loan WHERE book_id = ?")) {
            statement.setLong(1, bookId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Date dueDate = resultSet.getDate("due_date");
                    return dueDate.toLocalDate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching due date for book.", e);
        }

        return null; // Book with the given ID not found or no due date set
    }

    /**
     * Utility method to map a ResultSet to a Loan object
     */
    private Loan mapResultSetToLoan(ResultSet resultSet) throws SQLException {
        Loan loan = new Loan();
        loan.setId(resultSet.getLong("id"));
        loan.setMemberId(resultSet.getLong("member_id"));
        loan.setBookId(resultSet.getLong("book_id"));
        loan.setLoanDate(resultSet.getDate("loan_date").toLocalDate());
        loan.setDueDate(resultSet.getDate("due_date").toLocalDate());
        return loan;
    }
    /**
     *     Utility method to map a ResultSet to a Book object
      */
    private Book mapResultSetToBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setTitle(resultSet.getString("title"));
        book.setCategory(BookCategory.valueOf(resultSet.getString("category")));
        book.setPublicationDate(resultSet.getDate("publication_date").toLocalDate());
        book.setCopiesOwned(resultSet.getInt("copies_owned"));
        return book;
    }
}
