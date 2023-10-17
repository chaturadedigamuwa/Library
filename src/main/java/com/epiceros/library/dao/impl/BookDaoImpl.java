package com.epiceros.library.dao.impl;

import com.epiceros.library.dao.BookDao;
import com.epiceros.library.dao.MemberDao;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.entity.Author;
import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.BookCategory;
import com.epiceros.library.exception.BookNotFoundException;
import com.epiceros.library.exception.CopiesNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.lang.reflect.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDaoImpl implements BookDao {

    @Autowired
    private DataSource dataSource;

    @Override
    public Book getBookById(Long bookId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM book WHERE id = ?")) {
            statement.setLong(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToBook(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching book by ID", e);
        }
        return null; // Book with the given ID not found
    }

    @Override
    public void incrementCopiesOwned(Long bookId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE book SET copies_owned = copies_owned + 1 WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, bookId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error incrementing copies owned for book with ID " + bookId, e);
        }
    }


    @Override
    public void decrementCopiesOwned(Long bookId) {
        if (getCopiesOwned(bookId) > 0) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "UPDATE book SET copies_owned = copies_owned - 1 WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setLong(1, bookId);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error decrementing copies owned.", e);
            }
        } else throw new CopiesNotAvailableException("No copies available of book with ID " + bookId);
    }

    @Override
    public void saveBook(Book book) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO book (title, category, publication_date, copies_owned) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, book.getTitle());
                statement.setString(2, String.valueOf(book.getCategory()));
                statement.setDate(3, Date.valueOf(book.getPublicationDate()));
                statement.setInt(4, book.getCopiesOwned());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving book.", e);
        }
    }

    @Override
    public void saveAuthor(Author author) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO author (first_name, last_name) VALUES (?, ?)")) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving author.", e);
        }
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        List<Book> books = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM book WHERE category = ?")) {
            statement.setString(1, category);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = mapResultSetToBook(resultSet);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching books by category", e);
        }
        return books;
    }

    public int getCopiesOwned(Long bookId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT copies_owned FROM book WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, bookId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("copies_owned");
                    } else {
                        throw new BookNotFoundException("Book with ID " + bookId + " not found.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching copies owned.", e);
        }
    }

    /**
     * Utility method to map a ResultSet to a Book object
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
