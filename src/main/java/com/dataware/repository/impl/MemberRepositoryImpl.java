package com.dataware.repository.impl;

import com.dataware.database.DatabaseConnection;
import com.dataware.model.Member;
import com.dataware.model.enums.MemberRole;
import com.dataware.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberRepositoryImpl implements MemberRepository {

    private static final Logger logger = LoggerFactory.getLogger(MemberRepositoryImpl.class);  // Logger declaration

    private final Connection connection;

    public MemberRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addMember(Member member) {
        String sql = "INSERT INTO Member (first_name, last_name, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getFirstName());
            statement.setString(2, member.getLastName());
            statement.setString(3, member.getEmail());
            statement.setString(4, member.getRole().toString());
            statement.executeUpdate();
            logger.info("Member added successfully: {}", member.getEmail());  // Log success message
        } catch (SQLException e) {
            logger.error("Error adding member: {}", member.getEmail(), e);  // Log error message
        }
    }

    @Override
    public Optional<Member> getMemberById(int id) {
        String sql = "SELECT * FROM Member WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String roleStr = resultSet.getString("role");
                MemberRole role = MemberRole.fromString(roleStr);

                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        role
                );
                logger.info("Member found with id: {}", id);  // Log success message
                return Optional.of(member);
            }
        } catch (SQLException e) {
            logger.error("Error fetching member by id: {}", id, e);  // Log error message
        }
        logger.warn("Member not found with id: {}", id);  // Log warning if member not found
        return Optional.empty();
    }

    @Override
    public List<Member> getAllMembers(int page, int pageSize) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM Member LIMIT ? OFFSET ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, (page - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String roleStr = resultSet.getString("role");
                MemberRole role = MemberRole.fromString(roleStr);

                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        role
                );
                members.add(member);
            }
            logger.info("Fetched {} members for page {}", members.size(), page);  // Log success message
        } catch (SQLException e) {
            logger.error("Error fetching members for page: {}", page, e);  // Log error message
        }
        return members;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM Member";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String roleStr = resultSet.getString("role");
                MemberRole role = MemberRole.fromString(roleStr);

                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        role
                );
                members.add(member);
            }
            logger.info("Fetched {} members", members.size());  // Log success message
        } catch (SQLException e) {
            logger.error("Error fetching all members", e);  // Log error message
        }
        return members;
    }

    @Override
    public void updateMember(Member member) {
        String sql = "UPDATE Member SET first_name = ?, last_name = ?, email = ?, role = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getFirstName());
            statement.setString(2, member.getLastName());
            statement.setString(3, member.getEmail());
            statement.setString(4, member.getRole().toString());
            statement.setInt(5, member.getId());
            statement.executeUpdate();
            logger.info("Member updated successfully: {}", member.getEmail());  // Log success message
        } catch (SQLException e) {
            logger.error("Error updating member: {}", member.getEmail(), e);  // Log error message
        }
    }

    @Override
    public void deleteMember(int id) {
        String sql = "DELETE FROM Member WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Member deleted successfully with id: {}", id);  // Log success message
        } catch (SQLException e) {
            logger.error("Error deleting member with id: {}", id, e);  // Log error message
        }
    }

    @Override
    public int getTotalMembers() {
        String sql = "SELECT COUNT(*) FROM Member";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                int totalMembers = resultSet.getInt(1);
                logger.info("Total number of members: {}", totalMembers);  // Log success message
                return totalMembers;
            }
        } catch (SQLException e) {
            logger.error("Error fetching total number of members", e);  // Log error message
        }
        return 0;
    }

    @Override
    public List<Member> findByEmail(String email) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM Member WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String roleStr = resultSet.getString("role");
                MemberRole role = MemberRole.fromString(roleStr);
                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        role
                );
                members.add(member);
            }
            logger.info("Found {} members with email '{}'", members.size(), email);  // Log success message
        } catch (SQLException e) {
            logger.error("Error searching members by email: {}", email, e);  // Log error message
        }
        return members;
    }
}
