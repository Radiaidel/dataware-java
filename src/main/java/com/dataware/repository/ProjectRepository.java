package com.dataware.repository;
import java.util.List;

import com.dataware.model.Project;
import com.dataware.model.ProjectWithTeam;

public interface ProjectRepository {
	
	void createProject(Project project);
	
	ProjectWithTeam getProjectById(int id);
	 
	 List<Project> getAllProjects(int page, int size);
	 
	 void updateProject(Project project);
	 
	 void deleteProject(int id);

}
