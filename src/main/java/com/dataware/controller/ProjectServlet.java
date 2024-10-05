package com.dataware.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataware.model.Project;
import com.dataware.model.ProjectWithTeam;
import com.dataware.model.Team;
import com.dataware.model.enums.ProjectStatus;
import com.dataware.service.ProjectService;
import com.dataware.service.impl.ProjectServiceImpl;

import javax.servlet.RequestDispatcher;

public class ProjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProjectServiceImpl projectServiceImp;

    public void init() {
        projectServiceImp = new ProjectServiceImpl();
    }

    public ProjectServlet() {
        super();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listProject(request, response);
                    break;
                case "search":
                    searchProjects(request, response);
                    break;
                case "details":
                    displayDetails(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                case "addToProject":
                    addTeamToProject(request, response);
                    break;
                case "removeTeam":
                    removeTeamFromProject(request, response);
                    break;
                default:
                    listProject(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void displayDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        ProjectWithTeam projectWithTeam = projectServiceImp.getProjectById(id);

        if (projectWithTeam != null) {
            request.setAttribute("project", projectWithTeam.getProject());
            List<Team> teams = projectWithTeam.getTeams();
            request.setAttribute("teams", (teams != null) ? teams : new ArrayList<>());
            List<Team> availableTeams = projectServiceImp.getAvailableTeamsForProject(id);
            request.setAttribute("availableTeams", (availableTeams != null) ? availableTeams : new ArrayList<>());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/projects/showDetails.jsp");
            dispatcher.forward(request, response);
        } else {
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found.");
            }
        }
    }

    private void removeTeamFromProject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        int teamId = Integer.parseInt(request.getParameter("teamId"));

        projectServiceImp.removeTeamFromProject(projectId, teamId);

        response.sendRedirect(request.getContextPath() + "/projects?action=details&id=" + projectId);
    }

    private void addTeamToProject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        int teamId = Integer.parseInt(request.getParameter("teamId"));

        boolean success = projectServiceImp.addTeamToProject(projectId, teamId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/projects?action=details&id=" + projectId + "&success=TeamAdded");
        } else {
            request.setAttribute("errorMessage", "Failed to add team to project.");
            displayDetails(request, response); 
        }
    }

    private void searchProjects(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String query = request.getParameter("query");
        if (query == null || query.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid search query");
            return;
        }

        List<Project> searchResults = projectServiceImp.searchProjects(query);
        if (searchResults.isEmpty()) {
            request.setAttribute("message", "No projects found with the query: " + query);
        } else {
            request.setAttribute("projects", searchResults);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/projects/list.jsp");
        dispatcher.forward(request, response);
    }

    public void listProject(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int page = 1;
        int pageSize = 3;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int totalProjects = projectServiceImp.getProjectCount();
        int lastPage = (int) Math.ceil((double) totalProjects / pageSize);

        List<Project> projects = projectServiceImp.getAllProjects(page, pageSize);

        request.setAttribute("projects", projects);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalProjects", totalProjects);
        request.setAttribute("previousPage", page > 1 ? page - 1 : 1);
        request.setAttribute("nextPage", page < lastPage ? page + 1 : lastPage);
        request.setAttribute("lastPage", lastPage);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/projects/list.jsp");
        dispatcher.forward(request, response);
    }

    public void insertProject(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
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
        projectServiceImp.createProject(project);

        response.sendRedirect("projects");
    }

    private void deleteProject(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        projectServiceImp.deleteProject(id);
        response.sendRedirect("projects");
    }

    private void updateProject(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
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

        projectServiceImp.updateProject(updatedProject);

        response.sendRedirect("projects");
    }
}
