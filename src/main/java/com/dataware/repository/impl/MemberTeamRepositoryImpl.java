package com.dataware.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dataware.database.DatabaseConnection;
import com.dataware.model.Member;
import com.dataware.model.Team;
import com.dataware.model.enums.MemberRole;
import com.dataware.repository.MemberTeamRepository;

import org.slf4j.Logger; // Import SLF4J Logger
import org.slf4j.LoggerFactory; // Import LoggerFactory

public class MemberTeamRepositoryImpl implements MemberTeamRepository {

    private static final Logger logger = LoggerFactory.getLogger(MemberTeamRepositoryImpl.class); // Create logger instance
    private final Connection connection;

    public MemberTeamRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean addMemberToTeam(Member member, Team team) {
        String sql = "INSERT INTO Member_Team (member_id, team_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, member.getId());
            statement.setInt(2, team.getId());
            boolean success = statement.executeUpdate() > 0; 
            if (success) {
                logger.info("Member {} added to Team {}.", member.getId(), team.getId()); // Log successful addition
            } else {
                logger.warn("Failed to add Member {} to Team {}.", member.getId(), team.getId()); // Log warning if not successful
            }
            return success; 
        } catch (SQLException e) {
            logger.error("Error adding Member {} to Team {}: {}", member.getId(), team.getId(), e.getMessage()); // Log error
            return false;
        }
    }

    @Override
    public boolean removeMemberFromTeam(Member member, Team team) {
        String sql = "DELETE FROM Member_Team WHERE member_id = ? AND team_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, member.getId());
            statement.setInt(2, team.getId());
            boolean success = statement.executeUpdate() > 0; 
            if (success) {
                logger.info("Member {} removed from Team {}.", member.getId(), team.getId()); // Log successful removal
            } else {
                logger.warn("Failed to remove Member {} from Team {}.", member.getId(), team.getId()); // Log warning if not successful
            }
            return success; 
        } catch (SQLException e) {
            logger.error("Error removing Member {} from Team {}: {}", member.getId(), team.getId(), e.getMessage()); // Log error
            return false;
        }
    }

    @Override
    public List<Member> findMembersByTeamId(int teamId, int page, int pageSize) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.id, m.first_name, m.last_name, m.email, m.role " +
                     "FROM member m " +
                     "INNER JOIN Member_Team tm ON m.id = tm.member_id " +
                     "WHERE tm.team_id = ? " +
                     "LIMIT ? OFFSET ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set parameters: teamId, pageSize, and offset (calculated by page and pageSize)
            statement.setInt(1, teamId);
            statement.setInt(2, pageSize);
            statement.setInt(3, (page - 1) * pageSize);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String roleStr = rs.getString("role");
                MemberRole role = MemberRole.fromString(roleStr);

                Member member = new Member();
                member.setId(rs.getInt("id"));
                member.setFirstName(rs.getString("first_name"));
                member.setLastName(rs.getString("last_name"));
                member.setEmail(rs.getString("email")); // Get email
                member.setRole(role);  // Get role as an enum

                members.add(member);
            }
        } catch (SQLException e) {
            logger.error("Error finding members by Team ID {}: {}", teamId, e.getMessage()); // Log error
        }

        return members;
    }
}
