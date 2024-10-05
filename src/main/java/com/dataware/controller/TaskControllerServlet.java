package com.dataware.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataware.model.Member;
import com.dataware.model.Project;
import com.dataware.model.Task;
import com.dataware.model.enums.TaskPriority;
import com.dataware.model.enums.TaskStatus;
import com.dataware.service.impl.TaskServiceImpl;

public class TaskControllerServlet extends HttpServlet {

	private TaskServiceImpl taskServiceImpl;

	public TaskControllerServlet() {
		super();

	}
	public void setTaskServiceImpl(TaskServiceImpl taskServiceImpl) {
	    this.taskServiceImpl = taskServiceImpl;
	}
	
	@Override
	public void init() throws ServletException {
	    super.init();
	    taskServiceImpl = new TaskServiceImpl();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String projectIdParam = request.getParameter("id");
		String pageParam = request.getParameter("page");
		int pageNumber = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
		int pageSize = 10;

		if (projectIdParam != null && !projectIdParam.isEmpty()) {
			int projectId = Integer.parseInt(projectIdParam);

			Optional<List<Task>> optionalTasks = taskServiceImpl.getTasksByProjectId(projectId, pageNumber, pageSize);

			if (optionalTasks.isPresent()) {
				request.setAttribute("tasks", optionalTasks.get());
				request.setAttribute("currentPage", pageNumber);
				request.setAttribute("totalTasks", taskServiceImpl.getTotalTasks());

				RequestDispatcher dispatcher = request.getRequestDispatcher("/tasks/DisplayTasks.jsp");
				dispatcher.forward(request, response);
			} else {
				request.setAttribute("errorMessage", "No tasks found for this project.");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/tasks/DisplayTasks.jsp");
				dispatcher.forward(request, response);
			}
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Project ID is required.");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		switch (action) {
		case "addtask":
			try {
				addTask(request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "updatetask":
			updateTask(request);
			break;
		case "deletetask":
			deleteTask(request);
			break;
		}

		 String projectId = request.getParameter("projectId"); 
         response.sendRedirect(request.getContextPath() + "/projects?action=details&id=" + projectId);
	}

	private void addTask(HttpServletRequest request) throws Exception {
		try {
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String priorityStr = request.getParameter("priority");
			String statusStr = request.getParameter("status");
			String dueDate = request.getParameter("dueDate");
			String creationDate = request.getParameter("creationDate");
			int memberId = Integer.valueOf(request.getParameter("assignedMember"));
			int projectId = Integer.valueOf(request.getParameter("projectId"));

			validateTaskInput(title, description, priorityStr, statusStr, dueDate, creationDate);

			TaskPriority priority = TaskPriority.valueOf(priorityStr.toUpperCase());
			TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase());
			LocalDate parsedCreationDate = LocalDate.parse(creationDate);
			LocalDate parsedDueDate = LocalDate.parse(dueDate);

			Member member = new Member();
			member.setId(memberId);

			Project project = new Project();
			project.setId(projectId);

			Task newTask = new Task();
			newTask.setTitle(title);
			newTask.setDescription(description);
			newTask.setPriority(priority);
			newTask.setStatus(status);
			newTask.setCreationDate(parsedCreationDate);
			newTask.setDueDate(parsedDueDate);
			newTask.setProject(project);
			newTask.setMember(member);

			taskServiceImpl.addTask(newTask);

		} catch (IllegalArgumentException e) {
			throw new ServletException("Valeur de priorité ou de statut invalide : " + e.getMessage(), e);
		} catch (DateTimeParseException e) {
			throw new ServletException("Format de date invalide : " + e.getMessage(), e);
		}
	}

	private void updateTask(HttpServletRequest request) {
		int taskId = Integer.parseInt(request.getParameter("taskId"));
		Task task = new Task();

		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String priorityStr = request.getParameter("priority").trim();
		String statusStr = request.getParameter("status").trim();
		String dueDateStr = request.getParameter("dueDate");
		String creationDate = request.getParameter("creationDate");
		int memberId = Integer.valueOf(request.getParameter("assignedMember"));

		try {
			task.setId(taskId);
			task.setTitle(title);
			task.setDescription(description);
			task.setPriority(TaskPriority.valueOf(priorityStr.toUpperCase()));

			task.setStatus(TaskStatus.fromString(statusStr));
			task.setDueDate(LocalDate.parse(dueDateStr));
			task.setCreationDate(LocalDate.parse(creationDate));

			Member member = new Member();
			member.setId(memberId);
			task.setMember(member);


			taskServiceImpl.updateTask(task);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (DateTimeParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteTask(HttpServletRequest request) {
		int taskId = Integer.parseInt(request.getParameter("taskId"));
		taskServiceImpl.deleteTask(taskId);
	}

	private void validateTaskInput(String title, String description, String priorityStr, String statusStr,
			String dueDate, String creationDate) {
		if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty()
				|| priorityStr == null || statusStr == null || dueDate == null || creationDate == null) {
			throw new IllegalArgumentException("Tous les champs sont requis.");
		}
	}
}
