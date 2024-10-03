package com.dataware.repository.impl;

import java.util.List;

import com.dataware.database.DatabaseConnection;
import com.dataware.model.Project;
import com.dataware.model.enums.ProjectStatus;
import com.dataware.repository.ProjectRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository{

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
	            System.out.println("Project created successfully.");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		
	}

	@Override
	public Project getProjectById(int id) {
		 String sql = "SELECT * FROM project WHERE id = ?";
	        Project project = null;

	        try (Connection connection = DatabaseConnection.getInstance().getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	             
	            preparedStatement.setInt(1, id);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                project = new Project(
	                        resultSet.getString("name"),
	                        resultSet.getString("description"),
	                        resultSet.getDate("start_date").toLocalDate(),
	                        resultSet.getDate("end_date").toLocalDate(),
	                        ProjectStatus.valueOf(resultSet.getString("status"))
	                );
	                project.setId(resultSet.getInt("id"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return project;
	}

	@Override
	public List<Project> getAllProjects() {
		String sql = "SELECT * FROM project";
        List<Project> projects = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
             
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

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
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

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Project deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

}
