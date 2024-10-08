package com.dataware.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataware.model.Member;
import com.dataware.model.Team;
import com.dataware.repository.impl.MemberRepositoryImpl;
import com.dataware.repository.impl.MemberTeamRepositoryImpl;
import com.dataware.repository.impl.TeamRepositoryImpl;
import com.dataware.service.MemberService;
import com.dataware.service.MemberTeamService;
import com.dataware.service.TeamService;
import com.dataware.service.impl.MemberServiceImpl;
import com.dataware.service.impl.MemberTeamServiceImpl;
import com.dataware.service.impl.TeamServiceImpl;

// Import the SLF4J Logger and LoggerFactory classes
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(TeamServlet.class); // Create a logger instance

    private TeamService teamService;
    private MemberService memberService;
    private MemberTeamService memberTeamService;

    public TeamServlet() {
        super();
    }

 // New constructor for dependency injection in tests
    public TeamServlet(TeamService teamService, MemberService memberService, MemberTeamService memberTeamService) {
        this.teamService = teamService;
        this.memberService = memberService;
        this.memberTeamService = memberTeamService;
    }

    
    @Override
    public void init() throws ServletException {
        // Injecting the TeamRepositoryImpl into TeamServiceImpl
        teamService = new TeamServiceImpl(new TeamRepositoryImpl());
        memberService = new MemberServiceImpl(new MemberRepositoryImpl());
        memberTeamService = new MemberTeamServiceImpl(new MemberTeamRepositoryImpl());
        logger.info("TeamServlet initialized with TeamService, MemberService, and MemberTeamService.");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        logger.info("Received request with action: {}", action); // Log the received action

        switch (action) {
            case "list":
                listTeams(request, response);
                break;
            case "view":
                viewTeam(request, response);
                break;
            case "search":
                searchTeams(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                logger.error("Unknown action requested: {}", action); // Log unknown action
        }
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    private void listTeams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Pagination values
        int page = 1; // Default page
        int pageSize = 3; // Number of items per page

        // Get the current page number from the request
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        // Get total number of teams to calculate the last page
        int totalTeams = teamService.getTeamCount();
        int lastPage = (int) Math.ceil((double) totalTeams / pageSize);

        // Get the list of teams for the current page
        List<Team> teams = teamService.getAllTeams(page, pageSize);

        // Set attributes for the pagination controls
        request.setAttribute("teams", teams);
        request.setAttribute("currentPage", page);
        request.setAttribute("previousPage", page > 1 ? page - 1 : 1);
        request.setAttribute("nextPage", page < lastPage ? page + 1 : lastPage);
        request.setAttribute("lastPage", lastPage);

        logger.info("Listing teams: currentPage = {}, totalTeams = {}, lastPage = {}", page, totalTeams, lastPage); // Log pagination info

        // Forward to the JSP page
        request.getRequestDispatcher("/teams/teamList.jsp").forward(request, response);
    }

    private void viewTeam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int teamId = Integer.parseInt(request.getParameter("id"));
        Optional<Team> teamOpt = teamService.getTeamById(teamId);

        if (teamOpt.isPresent()) {
            Team team = teamOpt.get();
            request.setAttribute("team", team);

            // Pagination for members
            int page = 1; // Default page
            int pageSize = 3; // Number of members per page

            // Get the current page number from the request
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            // Get total number of members to calculate the last page
            int totalMembers = memberService.getTotalMembers();
            int lastPage = (int) Math.ceil((double) totalMembers / pageSize);

            // Get the list of members for the current page
            List<Member> availableMembers = memberService.getAllMembers(page, pageSize);
            List<Member> allMembers = memberService.getAllMembers();
            List<Member> membersoftheTeam = memberTeamService.getMembersOfTeam(team, page, pageSize);

            // Set attributes for pagination controls
            request.setAttribute("allMembers", allMembers);
            request.setAttribute("membersoftheTeam", membersoftheTeam);
            request.setAttribute("availableMembers", availableMembers);
            request.setAttribute("currentPage", page);
            request.setAttribute("previousPage", page > 1 ? page - 1 : 1);
            request.setAttribute("nextPage", page < lastPage ? page + 1 : lastPage);
            request.setAttribute("lastPage", lastPage);

            logger.info("Viewing team: id = {}, currentPage = {}, totalMembers = {}", teamId, page, totalMembers); // Log team viewing info

            // Forward to the JSP page
            request.getRequestDispatcher("/teams/teamView.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Team not found");
            logger.error("Team not found: id = {}", teamId); // Log error for team not found
        }
    }

    private void searchTeams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid team name");
            logger.warn("Invalid team name search request."); // Log invalid search request
            return;
        }

        List<Team> teams = teamService.searchTeamsByName(name);
        if (teams.isEmpty()) {
            request.setAttribute("message", "No teams found with the name: " + name);
            logger.info("No teams found with the name: {}", name); // Log search result
        } else {
            request.setAttribute("teams", teams);
            logger.info("Found {} teams with the name: {}", teams.size(), name); // Log found teams
        }

        request.getRequestDispatcher("/teams/teamList.jsp").forward(request, response);
    }

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.info("Received POST request with action: {}", action); // Log received POST action
        switch (action) {
            case "create":
                createTeam(request, response);
                break;
            case "update":
                updateTeam(request, response);
                break;
            case "delete":
                deleteTeam(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                logger.error("Unknown action in POST request: {}", action); // Log unknown POST action
        }
    }

    private void createTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");

        Team newTeam = new Team();
        newTeam.setName(name);
        teamService.addTeam(newTeam);
        
        logger.info("Team created: {}", name); // Log team creation

        response.sendRedirect("team?action=list");
    }

    private void updateTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");

        Team team = new Team();
        team.setId(id);
        team.setName(name);

        teamService.updateTeam(team);
        logger.info("Team updated: id = {}, name = {}", id, name); // Log team update
        response.sendRedirect("team?action=list");
    }

    private void deleteTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        teamService.deleteTeamById(id);
        
        logger.info("Team deleted: id = {}", id); // Log team deletion
        response.sendRedirect("team?action=list");
    }
}
