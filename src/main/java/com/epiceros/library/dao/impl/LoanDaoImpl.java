package com.epiceros.library.dao.impl;

import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.BookCategory;
import org.springframework.beans.factory.annotation.Autowired;
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
                statement.setDate(4, Date.valueOf(LocalDate.now().plusDays(30)));
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

    public void markBookAsReturned(Long loanId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE loan SET returned_date = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, Date.valueOf(LocalDate.now()));
                statement.setLong(2, loanId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error marking book as returned.", e);
        }
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
