package com.dataware.service.impl;

import com.dataware.model.Team;
import com.dataware.repository.TeamRepository;
import com.dataware.service.TeamService;

import java.util.List;
import java.util.Optional;

public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    // Constructor for Dependency Injection
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void addTeam(Team team) {
        teamRepository.addTeam(team);
    }

    @Override
    public Optional<Team> getTeamById(int id) {
        return teamRepository.getTeamById(id);
    }

    @Override
    public List<Team> getAllTeams(int page, int size) {
        return teamRepository.getAllTeams(page, size);
    }

    @Override
    public List<Team> searchTeamsByName(String name) {
        return teamRepository.searchTeamsByName(name);
    }

    @Override
    public void updateTeam(Team team) {
        teamRepository.updateTeam(team);
    }

    @Override
    public void deleteTeamById(int id) {
        teamRepository.deleteTeamById(id);
    }
    
    public int getTeamCount() {
        return teamRepository.getTotalTeams(); 
    }
}
