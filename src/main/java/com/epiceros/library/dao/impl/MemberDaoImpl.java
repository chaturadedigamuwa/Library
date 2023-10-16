package com.epiceros.library.dao.impl;

import com.epiceros.library.dao.MemberDao;
import com.epiceros.library.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class MemberDaoImpl implements MemberDao {

    @Autowired
    private DataSource dataSource;

    @Override
    public Member getMemberById(Long memberId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM member WHERE id = ?")) {
            statement.setLong(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToMember(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching member by ID", e);
        }
        return null; // Member with the given ID not found
    }

    /**
     *  Utility method to map a ResultSet to a Member object
     */
    private Member mapResultSetToMember(ResultSet resultSet) throws SQLException {
        Member member = new Member();
        member.setId(resultSet.getLong("id"));
        member.setFirstName(resultSet.getString("first_name"));
        member.setLastName(resultSet.getString("last_name"));
        // Set other properties as needed
        return member;
    }
}
