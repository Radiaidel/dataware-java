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




public class TeamServlet extends HttpServlet {
	
	 private TeamService teamService;
	 private MemberService memberService;
	 private MemberTeamService memberTeamService;

	 public TeamServlet() {
		 super();
		 // TODO Auto-generated constructor stub
	 }
	 
	 
	  @Override
	    public void init() throws ServletException {
	        // Injecting the TeamRepositoryImpl into TeamServiceImpl
	        teamService = new TeamServiceImpl(new TeamRepositoryImpl());
	        memberService = new MemberServiceImpl(new MemberRepositoryImpl()); // Initialize MemberService
	        memberService = new MemberServiceImpl(new MemberRepositoryImpl()); 
	        memberTeamService = new MemberTeamServiceImpl(new MemberTeamRepositoryImpl()); 

	    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			String action = request.getParameter("action");
			
			if(action == null) {
				
				action = "list";

			}
			
			switch(action) {
			
			case "list" :
				listTeams(request, response);
				break;
			case "view" : 
				viewTeam(request, response);
				break;
			 case "search":
	                searchTeams(request, response);
	                break;
			default :
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
				
			}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	private void listTeams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Pagination values
	    int page = 1;          // Default page
	    int pageSize = 3;      // Number of items per page

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
	            int totalMembers = memberService.getTotalMembers(); // Implement getMemberCount in MemberService
	            int lastPage = (int) Math.ceil((double) totalMembers / pageSize);

	            // Get the list of members for the current page
	            List<Member> availableMembers = memberService.getAllMembers(page, pageSize); // Implement getAllMembers in MemberService
	            List<Member> allMembers = memberService.getAllMembers(); // Implement getAllMembers in MemberService
	            List<Member> membersoftheTeam = memberTeamService.getMembersOfTeam(team, page, pageSize);

	            // Set attributes for pagination controls
	            request.setAttribute("allMembers", allMembers);
	            request.setAttribute("membersoftheTeam", membersoftheTeam);
	            request.setAttribute("availableMembers", availableMembers);
	            request.setAttribute("currentPage", page);
	            request.setAttribute("previousPage", page > 1 ? page - 1 : 1);
	            request.setAttribute("nextPage", page < lastPage ? page + 1 : lastPage);
	            request.setAttribute("lastPage", lastPage);

	            // Forward to the JSP page
	            request.getRequestDispatcher("/teams/teamView.jsp").forward(request, response);
	        } else {
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Team not found");
	        }
	    }
	   
	   private void searchTeams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String name = request.getParameter("name");
	        if (name == null || name.trim().isEmpty()) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid team name");
	            return;
	        }

	        List<Team> teams = teamService.searchTeamsByName(name);
	        if (teams.isEmpty()) {
	            request.setAttribute("message", "No teams found with the name: " + name);
	        } else {
	            request.setAttribute("teams", teams);
	        }

	        request.getRequestDispatcher("/teams/teamList.jsp").forward(request, response);
	    }
	

	   @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String action = request.getParameter("action");
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
	        }
	    }
	   
	    private void createTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        String name = request.getParameter("name");

	        Team newTeam = new Team();
	        newTeam.setName(name);
	        teamService.addTeam(newTeam);

	        response.sendRedirect("team?action=list");
	    }
	    
	    private void updateTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        int id = Integer.parseInt(request.getParameter("id"));
	        String name = request.getParameter("name");

	        Team team = new Team();
	        team.setId(id);
	        team.setName(name);

	        teamService.updateTeam(team);
	        response.sendRedirect("team?action=list");
	    }
	    

	    private void deleteTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        int id = Integer.parseInt(request.getParameter("id"));
	        teamService.deleteTeamById(id);
	        response.sendRedirect("team?action=list");
	    }
	    
}