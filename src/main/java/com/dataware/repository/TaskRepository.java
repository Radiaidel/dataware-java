package com.dataware.repository;

import java.util.List;
import java.util.Optional;

import com.dataware.model.Task;

public interface TaskRepository {
	boolean addTask(Task task);
	boolean updateTask(Task task);
	boolean deleteTask(int id);
	Optional<List<Task>> displayAll(int pageNumber, int pageSize);
	int getTotalTasks();
	Optional<Task> getTaskById(int id) ;
	int getLastInsertedId();

}
