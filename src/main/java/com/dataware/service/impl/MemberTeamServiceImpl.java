package com.dataware.service.impl;

import java.util.List;

import com.dataware.model.Member;
import com.dataware.model.Team;
import com.dataware.repository.MemberTeamRepository;
import com.dataware.service.MemberTeamService;

public class MemberTeamServiceImpl implements MemberTeamService {

    private final MemberTeamRepository memberTeamRepository;

    public MemberTeamServiceImpl(MemberTeamRepository memberTeamRepository) {
        this.memberTeamRepository = memberTeamRepository;
    }

    @Override
    public boolean addMemberToTeam(Member member, Team team) {
        return memberTeamRepository.addMemberToTeam(member, team);
    }

    @Override
    public boolean removeMemberFromTeam(Member member, Team team) {
        return memberTeamRepository.removeMemberFromTeam(member, team);
    }
    
    @Override
    public List<Member> getMembersOfTeam(Team team, int page, int pageSize) {
        return memberTeamRepository.findMembersByTeamId(team.getId(), page, pageSize);
    }
}
