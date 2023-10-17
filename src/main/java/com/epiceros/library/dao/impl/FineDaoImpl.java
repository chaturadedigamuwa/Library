package com.epiceros.library.dao.impl;

import com.epiceros.library.dao.FineDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@Repository
public class FineDaoImpl implements FineDao {

    @Autowired
    private DataSource dataSource;

    @Override
    public void saveFine(Long bookId, double fineAmount) {
        String sql = "INSERT INTO fine (book_id, fine_amount) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, bookId);
            statement.setDouble(2, fineAmount);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving fine.", e);
        }
    }
}
