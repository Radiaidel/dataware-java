package com.dataware.service;

import java.util.List;

import com.dataware.model.Member;
import com.dataware.model.Team;

public interface MemberTeamService {
    boolean addMemberToTeam(Member member, Team team);
    boolean removeMemberFromTeam(Member member, Team team);
    
    public List<Member> getMembersOfTeam(Team team, int page, int pageSize);
}
