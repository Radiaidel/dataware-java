package com.dataware.repository.impl;

import java.util.List;
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

    private final Connection connection;

    
	public ProjectRepositoryImpl(){
        this.connection = DatabaseConnection.getInstance().getConnection();
	}
	@Override
	public void createProject(Project project) {
		String sql = "INSERT INTO project (name, description, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
         
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	             
	            preparedStatement.setString(1, project.getName());
	            preparedStatement.setString(2, project.getDescription());
	            preparedStatement.setDate(3, Date.valueOf(project.getStartDate()));
	            preparedStatement.setDate(4, Date.valueOf(project.getEndDate()));
	            preparedStatement.setString(5, project.getStatus().name());

	            preparedStatement.executeUpdate();
	            System.out.println("Project created successfully.");
	        } catch (SQLException e) {
	            e.printStackTrace();
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

	    try ( PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

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
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return new ProjectWithTeam(project, teams);
	}
	@Override
	public List<Project> getAllProjects(int page, int size) {
	    List<Project> projects = new ArrayList<>();
	    String sql = "SELECT * FROM project LIMIT ? OFFSET ?"; // SQL for pagination

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return projects;
	}

	@Override
	public void updateProject(Project project) {
		String sql = "UPDATE project SET name = ?, description = ?, start_date = ?, end_date = ?, status = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getDescription());
            preparedStatement.setDate(3, Date.valueOf(project.getStartDate()));
            preparedStatement.setDate(4, Date.valueOf(project.getEndDate()));
            preparedStatement.setString(5, project.getStatus().name());
            preparedStatement.setInt(6, project.getId());

            preparedStatement.executeUpdate();
            System.out.println("Project updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public void deleteProject(int id) {
		String sql = "DELETE FROM project WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Project deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	public List<Project> searchProjects(String query) {
	    List<Project> projects = new ArrayList<>();
	    String sql = "SELECT * FROM project WHERE name LIKE ? OR description LIKE ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        String searchQuery = "%" + query + "%"; 
	        preparedStatement.setString(1, searchQuery);
	        preparedStatement.setString(2, searchQuery);
 
	        ResultSet rs = preparedStatement.executeQuery();

	        while (rs.next()) {
	            Project project = extractProjectFromResultSet(rs);
	            projects.add(project);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); 
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

	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
	             ResultSet resultSet = preparedStatement.executeQuery()) {

	            if (resultSet.next()) {
	                count = resultSet.getInt(1);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return count;
	}
    public int getLastInsertedId() {
        String query = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retourne -1 si aucun ID n'a été trouvé ou en cas d'erreur
    }

}