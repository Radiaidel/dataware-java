package com.dataware.controller;

import com.dataware.model.Member;
import com.dataware.model.Team;
import com.dataware.repository.impl.MemberRepositoryImpl;
import com.dataware.repository.impl.MemberTeamRepositoryImpl;
import com.dataware.repository.impl.TeamRepositoryImpl;
import com.dataware.service.MemberService;
import com.dataware.service.TeamService;
import com.dataware.service.impl.MemberServiceImpl;
import com.dataware.service.impl.MemberTeamServiceImpl;
import com.dataware.service.impl.TeamServiceImpl;
import com.dataware.service.MemberTeamService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger; // Import SLF4J Logger
import org.slf4j.LoggerFactory; // Import LoggerFactory

public class MemberTeamServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(MemberTeamServlet.class); // Create logger instance
    private MemberTeamService memberTeamService;
    private MemberService memberService;
    private TeamService teamService;

    public MemberTeamServlet() {
        super();
    }

    // New constructor for dependency injection in tests
    public MemberTeamServlet(TeamService teamService, MemberService memberService, MemberTeamService memberTeamService) {
        this.teamService = teamService;
        this.memberService = memberService;
        this.memberTeamService = memberTeamService;
    }

    @Override
    public void init() throws ServletException {
        // Injecting dependencies
        teamService = new TeamServiceImpl(new TeamRepositoryImpl());
        memberService = new MemberServiceImpl(new MemberRepositoryImpl());
        memberTeamService = new MemberTeamServiceImpl(new MemberTeamRepositoryImpl());
        logger.info("Services initialized."); // Log message on initialization
    }

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String memberIdParam = request.getParameter("memberId");
        String teamIdParam = request.getParameter("teamId");

        if (memberIdParam == null || teamIdParam == null) {
            logger.warn("Missing member or team ID."); // Log warning for missing IDs
            response.getWriter().write("Missing member or team ID.");
            return;
        }

        int memberId = Integer.parseInt(memberIdParam);
        int teamId = Integer.parseInt(teamIdParam);

        // Fetch member and team objects
        Optional<Member> memberOpt = memberService.getMemberById(memberId);
        Optional<Team> teamOpt = teamService.getTeamById(teamId);

        if (!memberOpt.isPresent() || !teamOpt.isPresent()) {
            logger.error("Member or Team not found. Member ID: {}, Team ID: {}", memberId, teamId); // Log error if not found
            response.getWriter().write("Member or Team not found.");
            return;
        }
        
        Member member = memberOpt.get();
        Team team = teamOpt.get();

        if ("add".equals(action)) {
            boolean added = memberTeamService.addMemberToTeam(member, team);
            if (added) {
                logger.info("Member {} added to Team {}.", memberId, teamId); // Log successful addition
                response.sendRedirect(request.getContextPath() + "/teams/team?action=view&id=" + teamId);
            } else {
                logger.error("Failed to add member {} to team {}.", memberId, teamId); // Log failure to add
                response.getWriter().write("Failed to add member to team.");
            }
        } else if ("remove".equals(action)) {
            boolean removed = memberTeamService.removeMemberFromTeam(member, team);
            if (removed) {
                logger.info("Member {} removed from Team {}.", memberId, teamId); // Log successful removal
                response.sendRedirect(request.getContextPath() + "/teams/team?action=view&id=" + teamId);
            } else {
                logger.error("Failed to remove member {} from team {}.", memberId, teamId); // Log failure to remove
                response.getWriter().write("Failed to remove member from team.");
            }
        } else {
            logger.warn("Invalid action received: {}", action); // Log warning for invalid action
            response.getWriter().write("Invalid action.");
        }
    }
}
