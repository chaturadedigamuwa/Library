package com.epiceros.library.dao.impl;

import com.epiceros.library.dao.MemberDao;
import com.epiceros.library.dto.response.MemberWithFine;
import com.epiceros.library.entity.Member;
import com.epiceros.library.entity.MemberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public MemberStatus getMemberStatus(Long memberId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT ms.* FROM member m " +
                     "JOIN member_status ms ON m.active_status_id = ms.id WHERE m.id = ?")) {
            statement.setLong(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    MemberStatus status = MemberStatus.valueOf(resultSet.getString("status_value"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching member status by ID", e);
        }
        return MemberStatus.UNKNOWN; // Handle case when member status is not found
    }

    @Override
    public List<MemberWithFine> getMembersWithFines() {
        List<MemberWithFine> membersWithFines = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT m.id AS member_id, m.first_name, m.last_name, SUM(f.fine_amount) AS total_fine " +
                             "FROM member m " +
                             "JOIN loan l ON m.id = l.member_id " +
                             "LEFT JOIN fine f ON l.id = f.loan_id " +
                             "GROUP BY m.id, m.first_name, m.last_name")) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    MemberWithFine memberWithFines = new MemberWithFine();
                    memberWithFines.setMemberId(resultSet.getLong("member_id"));
                    memberWithFines.setFirstName(resultSet.getString("first_name"));
                    memberWithFines.setLastName(resultSet.getString("last_name"));
                    memberWithFines.setTotalFine(resultSet.getInt("total_fine"));
                    membersWithFines.add(memberWithFines);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching members with fines", e);
        }

        return membersWithFines;
    }

    /**
     *  Utility method to map a ResultSet to a Member object
     */
    private Member mapResultSetToMember(ResultSet resultSet) throws SQLException {
        Member member = new Member();
        member.setId(resultSet.getLong("id"));
        member.setFirstName(resultSet.getString("first_name"));
        member.setLastName(resultSet.getString("last_name"));
        return member;
    }
}
