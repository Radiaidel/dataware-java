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

public class TeamRepositoryImpl implements TeamRepository {


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
        } catch (SQLException e) {
            e.printStackTrace();
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
                return Optional.of(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteTeamById(int id) {
        String sql = "DELETE FROM Team WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalTeams;
    }

    

}
