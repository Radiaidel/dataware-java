package com.dataware.repository;

import java.util.List;

import com.dataware.model.Member;
import com.dataware.model.Team;

public interface MemberTeamRepository {

    boolean addMemberToTeam(Member member, Team team);
    boolean removeMemberFromTeam(Member member, Team team);

    List<Member> findMembersByTeamId(int teamId, int page, int pageSize);

    
}
