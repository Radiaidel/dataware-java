package com.dataware.repository.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import com.dataware.database.DatabaseConnection;
import com.dataware.model.Project;
import com.dataware.model.ProjectWithTeam;
import com.dataware.model.Team;
import com.dataware.model.enums.ProjectStatus;
import com.dataware.repository.ProjectRepository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository{
    private static final Logger logger = LoggerFactory.getLogger(ProjectRepositoryImpl.class);

	@Override
	public void createProject(Project project) {
		String sql = "INSERT INTO project (name, description, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
         
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	             
	            preparedStatement.setString(1, project.getName());
	            preparedStatement.setString(2, project.getDescription());
	            preparedStatement.setDate(3, Date.valueOf(project.getStartDate()));
	            preparedStatement.setDate(4, Date.valueOf(project.getEndDate()));
	            preparedStatement.setString(5, project.getStatus().name());

	            preparedStatement.executeUpdate();
	            logger.info("Project '{}' created successfully.", project.getName());
	        } catch (SQLException e) {
	        	logger.error("Failed to create project '{}': {}", project.getName(), e.getMessage());
	        }
		
	}

	@Override
	public ProjectWithTeam getProjectById(int id) {
	    
	    String sql = "SELECT p.*, t.id AS team_id, t.name AS team_name FROM project p " +
	                 "LEFT JOIN project_team pt ON p.id = pt.project_id " +
	                 "LEFT JOIN team t ON pt.team_id = t.id " +
	                 "WHERE p.id = ?";

	    Project project = null;
	    List<Team> teams = new ArrayList<>();

	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        preparedStatement.setInt(1, id);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            
	            if (project == null) {
	                project = new Project(
	                    resultSet.getString("name"),
	                    resultSet.getString("description"),
	                    resultSet.getDate("start_date").toLocalDate(),
	                    resultSet.getDate("end_date").toLocalDate(),
	                    ProjectStatus.valueOf(resultSet.getString("status"))
	                );
	                project.setId(resultSet.getInt("id"));
	            }

	            
	            int teamId = resultSet.getInt("team_id");
	            String teamName = resultSet.getString("team_name");

	            
	            if (teamId > 0 && teamName != null) {
	                Team team = new Team(); 
	                team.setId(teamId); 
	                team.setName(teamName); 
	                teams.add(team);
	            }
	        }
	        logger.info("Retrieved project with ID {}: {}", id, project);
	    } catch (SQLException e) {
	        logger.error("Failed to retrieve project with ID {}: {}", id, e.getMessage());
	    }
	    
	    return new ProjectWithTeam(project, teams);
	}
	
	public List<Team> getAvailableTeamsForProject(int projectId) {
	    String sql = "SELECT t.* FROM team t " +
	                 "LEFT JOIN project_team pt ON t.id = pt.team_id " +
	                 "LEFT JOIN project p ON pt.project_id = p.id " +
	                 "WHERE pt.team_id IS NULL " + 
	                 "   OR p.status = 'Completed'";

	    List<Team> availableTeams = new ArrayList<>();

	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        ResultSet resultSet = preparedStatement.executeQuery();
	        while (resultSet.next()) {	        
	            Team team = new Team();
	            team.setId(resultSet.getInt("id"));
	            team.setName(resultSet.getString("name"));
	            availableTeams.add(team);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return availableTeams;
	}
	public boolean addTeamToProject(int projectId, int teamId) {
	    String sql = "INSERT INTO project_team (project_id, team_id) VALUES (?, ?)";

	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        preparedStatement.setInt(1, projectId);
	        preparedStatement.setInt(2, teamId);
	        int rowsAffected = preparedStatement.executeUpdate();

	        logger.info("Added team ID {} to project ID {}.", teamId, projectId);
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        logger.error("Failed to add team ID {} to project ID {}: {}", teamId, projectId, e.getMessage());
	    }
	    return false;
	}
	
	public void removeTeamFromProject(int projectId, int teamId) {
	    String sql = "DELETE FROM project_team WHERE project_id = ? AND team_id = ?";

	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        
	        preparedStatement.setInt(1, projectId);
	        preparedStatement.setInt(2, teamId);
	        preparedStatement.executeUpdate();

	        logger.info("Removed team ID {} from project ID {}.", teamId, projectId);
	    } catch (SQLException e) {
	        logger.error("Failed to remove team ID {} from project ID {}: {}", teamId, projectId, e.getMessage());
	    }
	}

	@Override
	public List<Project> getAllProjects(int page, int size) {
	    List<Project> projects = new ArrayList<>();
	    String sql = "SELECT * FROM project LIMIT ? OFFSET ?"; // SQL for pagination

	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, size); // Set the page size (number of items per page)
	        stmt.setInt(2, (page - 1) * size); // Set the offset for pagination

	        ResultSet resultSet = stmt.executeQuery();
	        while (resultSet.next()) {
	            Project project = new Project(
	                    resultSet.getString("name"),
	                    resultSet.getString("description"),
	                    resultSet.getDate("start_date").toLocalDate(),
	                    resultSet.getDate("end_date").toLocalDate(),
	                    ProjectStatus.valueOf(resultSet.getString("status"))
	            );
	            project.setId(resultSet.getInt("id"));
	            projects.add(project);
	        }
	        logger.info("Retrieved {} projects for page {}.", projects.size(), page);
	    } catch (SQLException e) {
	        logger.error("Failed to retrieve projects for page {}: {}", page, e.getMessage());
	    }
	    return projects;
	}

	@Override
	public void updateProject(Project project) {
		String sql = "UPDATE project SET name = ?, description = ?, start_date = ?, end_date = ?, status = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getDescription());
            preparedStatement.setDate(3, Date.valueOf(project.getStartDate()));
            preparedStatement.setDate(4, Date.valueOf(project.getEndDate()));
            preparedStatement.setString(5, project.getStatus().name());
            preparedStatement.setInt(6, project.getId());

            preparedStatement.executeUpdate();
            logger.info("Project ID {} updated successfully.", project.getId());
        } catch (SQLException e) {
            logger.error("Failed to update project ID {}: {}", project.getId(), e.getMessage());
        }
		
	}

	@Override
	public void deleteProject(int id) {
		String sql = "DELETE FROM project WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            logger.info("Project ID {} deleted successfully.", id);
        } catch (SQLException e) {
            logger.error("Failed to delete project ID {}: {}", id, e.getMessage());
        }
		
	}
	
	public List<Project> searchProjects(String query) {
	    List<Project> projects = new ArrayList<>();
	    String sql = "SELECT * FROM project WHERE name LIKE ? OR description LIKE ?";

	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        String searchQuery = "%" + query + "%"; 
	        preparedStatement.setString(1, searchQuery);
	        preparedStatement.setString(2, searchQuery);
 
	        ResultSet rs = preparedStatement.executeQuery();

	        while (rs.next()) {
	            Project project = extractProjectFromResultSet(rs);
	            projects.add(project);
	        }

	        logger.info("Found {} projects matching the query '{}'.", projects.size(), query);
	    } catch (SQLException e) {
	        logger.error("Failed to search project for query '{}': {}", query, e.getMessage());
	    }

	    return projects; 
	}
    // Helper method to extract project data from the ResultSet
    private Project extractProjectFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate startDate = rs.getDate("start_date").toLocalDate();
        LocalDate endDate = rs.getDate("end_date").toLocalDate();
        ProjectStatus status = ProjectStatus.valueOf(rs.getString("status"));

        return new Project( name, description, startDate, endDate, status);
    }

	public int getProjectCount() {
		 int count = 0;
	        String sql = "SELECT COUNT(*) FROM project"; // Adjust table name if needed

	        try (Connection connection = DatabaseConnection.getInstance().getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql);
	             ResultSet resultSet = preparedStatement.executeQuery()) {

	            if (resultSet.next()) {
	                count = resultSet.getInt(1);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return count;
	}

}
