package com.dataware.model;

import java.time.LocalDate;

import com.dataware.model.enums.ProjectStatus;

public class Project {
	private int id;
	private String name;
	private String description;
	 private LocalDate startDate;  // changed from start_date
	    private LocalDate endDate;
	private ProjectStatus   status;
	
	public Project(String name,String description,LocalDate startDate,LocalDate endDate,ProjectStatus status) {
		 this.name=name;
		 this.description=description;
		 this.startDate = startDate;  // changed from start_date
	      this.endDate = endDate;
		 this.status=status;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description){
		this.description=description;
	}
	
	// Getters and setters (with camel case)
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
	    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
