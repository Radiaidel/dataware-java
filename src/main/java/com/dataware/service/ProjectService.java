package com.dataware.service;


import java.util.List;

import com.dataware.model.Project;
import com.dataware.model.ProjectWithTeam;
import com.dataware.model.Team;

public interface ProjectService {

    void createProject(Project project);

    ProjectWithTeam getProjectById(int id);

    List<Team> getAvailableTeamsForProject(int projectId);

    boolean addTeamToProject(int projectId, int teamId);

    void removeTeamFromProject(int projectId, int teamId);

    List<Project> getAllProjects(int page, int size);

    void updateProject(Project project);

    void deleteProject(int id);

    List<Project> searchProjects(String query);

    int getProjectCount();
}
