package com.dataware.service.impl;

import com.dataware.model.Project;
import com.dataware.model.ProjectWithTeam;
import com.dataware.model.Team;
import com.dataware.repository.impl.ProjectRepositoryImpl;
import com.dataware.service.ProjectService;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private ProjectRepositoryImpl projectRepository;

    public ProjectServiceImpl() {
        this.projectRepository = new ProjectRepositoryImpl();
    }

    @Override
    public void createProject(Project project) {
        projectRepository.createProject(project);
    }

    @Override
    public ProjectWithTeam getProjectById(int id) {
        return projectRepository.getProjectById(id);
    }

    @Override
    public List<Team> getAvailableTeamsForProject(int projectId) {
        return projectRepository.getAvailableTeamsForProject(projectId);
    }

    @Override
    public boolean addTeamToProject(int projectId, int teamId) {
        return projectRepository.addTeamToProject(projectId, teamId);
    }

    @Override
    public void removeTeamFromProject(int projectId, int teamId) {
        projectRepository.removeTeamFromProject(projectId, teamId);
    }

    @Override
    public List<Project> getAllProjects(int page, int size) {
        return projectRepository.getAllProjects(page, size);
    }

    @Override
    public void updateProject(Project project) {
        projectRepository.updateProject(project);
    }

    @Override
    public void deleteProject(int id) {
        projectRepository.deleteProject(id);
    }

    @Override
    public List<Project> searchProjects(String query) {
        return projectRepository.searchProjects(query);
    }

    @Override
    public int getProjectCount() {
        return projectRepository.getProjectCount();
    }
}

