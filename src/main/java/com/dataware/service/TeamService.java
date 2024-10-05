package com.dataware.service;

import com.dataware.model.Team;
import java.util.List;
import java.util.Optional;

public interface TeamService {

    // 1. Create (Add a new team)
    void addTeam(Team team);

    // 2. Read (Get a team by ID)
    Optional<Team> getTeamById(int id);

    // 3. Read (Get all teams with pagination)
    List<Team> getAllTeams(int page, int size);

    // 4. Search teams by name (used for team search functionality)
    List<Team> searchTeamsByName(String name);

    // 5. Update (Update a team)
    void updateTeam(Team team);

    // 6. Delete (Delete a team by ID)
    void deleteTeamById(int id);
    
    public int getTeamCount();
}
