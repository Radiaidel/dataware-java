package com.dataware.service.impl;

import java.util.List;
import java.util.Optional;

import com.dataware.model.Task;
import com.dataware.repository.TaskRepository;
import com.dataware.repository.impl.TaskRepositoryImpl;
import com.dataware.service.TaskService;

public class TaskServiceImpl implements TaskService {

	private TaskRepository taskRepository;

	public TaskServiceImpl() {
		taskRepository = new TaskRepositoryImpl();
	}

	public void addTask(Task task) throws Exception {

		taskRepository.addTask(task);
	}

	public Optional<List<Task>> displayAll(int pageNumber, int pageSize) {
		return taskRepository.displayAll(pageNumber, pageSize);
	}

	public int getTotalTasks() {
		return taskRepository.getTotalTasks();
	}

}
