package com.dataware.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataware.model.Project;
import com.dataware.model.enums.ProjectStatus;
import com.dataware.repository.impl.ProjectRepositoryImpl;

import javax.servlet.RequestDispatcher;

/**
 * Servlet implementation class ProjectServlet
 */

public class ProjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectRepositoryImpl projectRepo;

    public void init() {
        projectRepo = new ProjectRepositoryImpl();
    }

    public ProjectServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/":
                default:
                    listProject(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "insert":
                    insertProject(request, response);
                    break;
                case "update":
                    updateProject(request, response);
                    break;
                case "delete":
                    deleteProject(request, response);
                    break;
                default:
                	   listProject(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    // Methods that handle specific actions (same as before)
    
    private void listProject(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException {
        List<Project> listProject = projectRepo.getAllProjects();
        request.setAttribute("listProject", listProject);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/projects/list.jsp");
        dispatcher.forward(request, response);
    }

    private void insertProject(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String startDateStr = request.getParameter("start_date");
        String endDateStr = request.getParameter("end_date");
        String statusStr = request.getParameter("status");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        ProjectStatus status = ProjectStatus.valueOf(statusStr);
        Project project = new Project(name, description, startDate, endDate, status);
        projectRepo.createProject(project);

        response.sendRedirect("projects");
    }

    private void deleteProject(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        projectRepo.deleteProject(id);
        response.sendRedirect("projects");
    }

    private void updateProject(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String startDateStr = request.getParameter("start_date");
        String endDateStr = request.getParameter("end_date");
        String statusStr = request.getParameter("status");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
        ProjectStatus status = ProjectStatus.valueOf(statusStr);

        Project updatedProject = new Project(name, description, startDate, endDate, status);
        updatedProject.setId(id);

        projectRepo.updateProject(updatedProject);

        response.sendRedirect("projects");
    }
}

