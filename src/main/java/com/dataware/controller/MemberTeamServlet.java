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

public class MemberTeamServlet extends HttpServlet {
    
    private  MemberTeamService memberTeamService;
    private  MemberService memberService;
    private  TeamService teamService;
    
    public MemberTeamServlet() {
    	super();
    	// TODO Auto-generated constructor stub
    }

    public void init() throws ServletException {
        // Injecting dependencies
    	teamService = new TeamServiceImpl(new TeamRepositoryImpl());
        memberService = new MemberServiceImpl(new MemberRepositoryImpl());
        memberTeamService = new MemberTeamServiceImpl(new MemberTeamRepositoryImpl());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String memberIdParam = request.getParameter("memberId");
        String teamIdParam = request.getParameter("teamId");

        if (memberIdParam == null || teamIdParam == null) {
            response.getWriter().write("Missing member or team ID.");
            return;
        }

        int memberId = Integer.parseInt(memberIdParam);
        int teamId = Integer.parseInt(teamIdParam);

        // Fetch member and team objects
        Optional<Member> memberOpt = memberService.getMemberById(memberId);
        Optional<Team> teamOpt = teamService.getTeamById(teamId);

        if (!memberOpt.isPresent() || !teamOpt.isPresent()) {
            response.getWriter().write("Member or Team not found.");
            return;
        }
        
        Member member = memberOpt.get();
        Team team = teamOpt.get();

        if ("add".equals(action)) {
            boolean added = memberTeamService.addMemberToTeam(member, team);
            if (added) {
                response.sendRedirect(request.getContextPath() + "/teams/team?action=view&id=" + teamId);
            } else {
                response.getWriter().write("Failed to add member to team.");
            }
        } else if ("remove".equals(action)) {
            boolean removed = memberTeamService.removeMemberFromTeam(member, team);
            if (removed) {
                response.sendRedirect(request.getContextPath() + "/teams/team?action=view&id=" + teamId);
            } else {
                response.getWriter().write("Failed to remove member from team.");
            }
        } else {
            response.getWriter().write("Invalid action.");
        }
    }
}