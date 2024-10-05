package com.dataware.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataware.model.Member;
import com.dataware.model.Project;
import com.dataware.model.ProjectWithTeam;
import com.dataware.model.Team;
import com.dataware.model.enums.ProjectStatus;
import com.dataware.repository.impl.MemberTeamRepositoryImpl;
import com.dataware.repository.impl.ProjectRepositoryImpl;
import com.dataware.service.MemberTeamService;
import com.dataware.service.impl.MemberTeamServiceImpl;

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
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list"; // Default action
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
                	displayDetails(request,response);
                	break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
  
//    private void displayDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        
//        ProjectWithTeam projectWithTeam = projectRepo.getProjectById(id); // Fetch project and teams
//
//        if (projectWithTeam != null) {
//            // Project exists, set the project and teams attributes
//            request.setAttribute("project", projectWithTeam.getProject());
//            
//            // If teams is null or empty, ensure proper handling
//            List<Team> teams = projectWithTeam.getTeams();
//            request.setAttribute("teams", (teams != null) ? teams : new ArrayList<>());
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/projects/showDetails.jsp");
//            dispatcher.forward(request, response);
//            
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/tasks"); // URL du TaskControllerServlet
//            dispatcher.forward(request, response);
//        } else {
//            if (!response.isCommitted()) {
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found.");
//            }
//        }
//    }
    
    
    private void displayDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        ProjectWithTeam projectWithTeam = projectRepo.getProjectById(id);
        MemberTeamService memberTeamService = new MemberTeamServiceImpl(new MemberTeamRepositoryImpl());
        
        if (projectWithTeam != null) {
            // Récupérer le projet et l'ensemble des équipes
            request.setAttribute("project", projectWithTeam.getProject());

            List<Team> teams = projectWithTeam.getTeams();
            request.setAttribute("teams", (teams != null) ? teams : new ArrayList<>());

            // Récupérer tous les membres de toutes les équipes
            List<Member> allMembers = new ArrayList<>();
            for (Team team : teams) {
                List<Member> membersOfTheTeam = memberTeamService.getMembersOfTeam(team, 1, 10); // Récupérer les membres de l'équipe
                allMembers.addAll(membersOfTheTeam);
            }
            request.setAttribute("members", allMembers);
            
            // Passer l'ID du projet à la requête pour le TaskControllerServlet
            request.setAttribute("projectId", id);
            
            // Redirection vers le TaskControllerServlet
            RequestDispatcher dispatcher = request.getRequestDispatcher("/tasks");
            dispatcher.forward(request, response);
            
        } else {
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found.");
            }
        }
    }


    
    private void searchProjects(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        String query = request.getParameter("query");
        if (query == null || query.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid search query");
            return;
        }

        List<Project> searchResults = projectRepo.searchProjects(query);
        if (searchResults.isEmpty()) {
            request.setAttribute("message", "No projects found with the query: " + query);
        } else {
            request.setAttribute("listProject", searchResults);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/projects/list.jsp");
        dispatcher.forward(request, response);
    }

    
    private void listProject(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
      
        int page = 1;       
        int pageSize = 3;      
       
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int totalProjects = projectRepo.getProjectCount(); 
        int lastPage = (int) Math.ceil((double) totalProjects / pageSize);

        List<Project> projects = projectRepo.getAllProjects(page, pageSize); 

        request.setAttribute("projects", projects);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalProjects", totalProjects);
        request.setAttribute("previousPage", page > 1 ? page - 1 : 1);
        request.setAttribute("nextPage", page < lastPage ? page + 1 : lastPage);
        request.setAttribute("lastPage", lastPage);

        
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

