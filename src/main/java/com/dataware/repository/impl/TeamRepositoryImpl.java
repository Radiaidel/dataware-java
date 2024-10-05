package com.dataware.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dataware.database.DatabaseConnection;
import com.dataware.model.Team;
import com.dataware.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamRepositoryImpl implements TeamRepository {

    private static final Logger logger = LoggerFactory.getLogger(TeamRepositoryImpl.class);  // Logger declaration

    private final Connection connection;

    public TeamRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    @Override
    public void addTeam(Team team) {
        String sql = "INSERT INTO Team (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, team.getName());
            stmt.executeUpdate();
            logger.info("Team added successfully: {}", team.getName());  // Log success message
        } catch (SQLException e) {
            logger.error("Error adding team: {}", team.getName(), e);  // Log error message
        }
    }
    
    @Override
    public Optional<Team> getTeamById(int id) {
        String sql = "SELECT * FROM Team WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Team team = new Team(rs.getInt("id"), rs.getString("name"));
                logger.info("Team found with id: {}", id);  // Log success message
                return Optional.of(team);
            }
        } catch (SQLException e) {
            logger.error("Error fetching team by id: {}", id, e);  // Log error message
        }
        logger.warn("Team not found with id: {}", id);  // Log warning if team not found
        return Optional.empty();
    }
    
    @Override
    public List<Team> getAllTeams(int page, int size) {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM Team LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, size);
            stmt.setInt(2, (page - 1) * size);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                teams.add(new Team(rs.getInt("id"), rs.getString("name")));
            }
            logger.info("Fetched {} teams for page {}", teams.size(), page);  // Log success message
        } catch (SQLException e) {
            logger.error("Error fetching teams for page: {}", page, e);  // Log error message
        }
        return teams;
    }
    
    @Override
    public void updateTeam(Team team) {
        String sql = "UPDATE Team SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, team.getName());
            stmt.setInt(2, team.getId());
            stmt.executeUpdate();
            logger.info("Team updated successfully: {}", team.getName());  // Log success message
        } catch (SQLException e) {
            logger.error("Error updating team: {}", team.getName(), e);  // Log error message
        }
    }
    
    @Override
    public void deleteTeamById(int id) {
        String sql = "DELETE FROM Team WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Team deleted successfully with id: {}", id);  // Log success message
        } catch (SQLException e) {
            logger.error("Error deleting team with id: {}", id, e);  // Log error message
        }
    }

    @Override
    public List<Team> searchTeamsByName(String name) {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM Team WHERE name LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                teams.add(new Team(rs.getInt("id"), rs.getString("name")));
            }
            logger.info("Found {} teams with name containing '{}'", teams.size(), name);  // Log success message
        } catch (SQLException e) {
            logger.error("Error searching teams by name: {}", name, e);  // Log error message
        }
        return teams;
    }
    
    public int getTotalTeams() {
        int totalTeams = 0;
        String sql = "SELECT COUNT(*) FROM team";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                totalTeams = resultSet.getInt(1);  
            }
            logger.info("Total number of teams: {}", totalTeams);  // Log success message
        } catch (SQLException e) {
            logger.error("Error fetching total number of teams", e);  // Log error message
        }
        return totalTeams;
    }
}
