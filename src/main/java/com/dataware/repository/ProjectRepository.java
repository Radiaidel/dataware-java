package com.dataware.repository;
import java.util.List;

import com.dataware.model.Project;

public interface ProjectRepository {
	
	void createProject(Project project);
	
	 Project getProjectById(int id);
	 
	 List<Project> getAllProjects();
	 
	 void updateProject(Project project);
	 
	 void deleteProject(int id);

}
