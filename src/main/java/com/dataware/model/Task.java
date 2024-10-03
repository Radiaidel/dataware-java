package com.dataware.model;

import com.dataware.model.enums.TaskStatus;

import java.sql.Date;
import java.time.LocalDate;

import com.dataware.model.enums.TaskPriority;

public class Task {
	private int id;
	private String title;
	private String description;
	private TaskPriority priority;
	private TaskStatus status;
	private LocalDate creationDate;
	private LocalDate dueDate;
	private Member member;
	private Project project;

	public Task() {
		
	}
	
	

	public Task(int id, String title, String description, TaskPriority priority, TaskStatus status,
			LocalDate creationDate, LocalDate dueDate, Member member, Project project) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.status = status;
		this.creationDate = creationDate;
		this.dueDate = dueDate;
		this.member = member;
		this.project = project;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Member getMember() {
		return member;
	}


	public void setMember(Member member) {
		this.member = member;
	}



	public Project getProject() {
		return project;
	}



	public void setProject(Project project) {
		this.project = project;
	}



	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", description=" + description + ", priority=" + priority
				+ ", status=" + status + ", creationDate=" + creationDate + ", dueDate=" + dueDate + "]";
	}

	
}
