package com.dataware.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void deleteTask(int taskId) {
   
        taskRepository.deleteTask(taskId);
    }
    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }
    
    public Optional<List<Task>> getTasksByProjectId(int projectId, int pageNumber, int pageSize) {
        Optional<List<Task>> optionalTasks = taskRepository.displayAll(pageNumber, pageSize);

        if (optionalTasks.isPresent()) {
            List<Task> filteredTasks = optionalTasks.get().stream()
                .filter(task -> task.getProject().getId() == projectId)
                .skip((pageNumber - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

            return Optional.of(filteredTasks);
        } else {
            return Optional.empty(); 
        }
    }

}
