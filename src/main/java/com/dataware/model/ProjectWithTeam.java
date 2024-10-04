package com.dataware.model;
import java.util.ArrayList;
import java.util.List;

public class ProjectWithTeam {
    private Project project; // Assuming you have a Project class
    private List<Team> teams; // Assuming you have a Team class

    public ProjectWithTeam(Project project, List<Team> teams) {
        this.project = project;
        this.teams = teams;
    }

    public Project getProject() {
        return project;
    }

    public List<Team> getTeams() {
        return teams != null ? teams : new ArrayList<>(); 
    }
}
