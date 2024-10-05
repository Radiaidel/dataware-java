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

	public void init() {

		taskServiceImpl = new TaskServiceImpl();
	}

//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		String action = request.getParameter("action");
//
//		if ("addtask".equals(action)) {
//			String title = request.getParameter("title");
//			String description = request.getParameter("description");
//			String priorityStr = request.getParameter("priority");
//			String statusStr = request.getParameter("status");
//			String dueDate = request.getParameter("dueDate");
//			String creationDate = request.getParameter("creationDate");
//
//			if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty()
//					|| priorityStr == null || statusStr == null || dueDate == null || creationDate == null) {
//				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tous les champs sont requis.");
//				return;
//			}
//
//			try {
//				TaskPriority priority = TaskPriority.valueOf(priorityStr.toUpperCase());
//				TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase());
//
//				LocalDate parsedCreationDate = LocalDate.parse(creationDate);
//				LocalDate parsedDueDate = LocalDate.parse(dueDate);
//
//				Member member = new Member();
//				member.setId(1);
//
//				Project project = new Project();
//				project.setId(1);
//
//				Task newTask = new Task();
//				newTask.setTitle(title);
//				newTask.setDescription(description);
//				newTask.setPriority(priority);
//				newTask.setStatus(status);
//				newTask.setCreationDate(parsedCreationDate);
//				newTask.setDueDate(parsedDueDate);
//				newTask.setProject(project);
//				newTask.setMember(member);
//
//				taskServiceImpl.addTask(newTask);
//
//			} catch (IllegalArgumentException e) {
//				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
//						"Valeur de priorité ou de statut invalide : " + e.getMessage());
//			} catch (DateTimeParseException e) {
//				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Format de date invalide : " + e.getMessage());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		else if ("deletetask".equals(action)) {
//            String taskIdStr = request.getParameter("taskId");
//            try {
//                int taskId = Integer.parseInt(taskIdStr);
//                taskServiceImpl.deleteTask(taskId); 
//                
//            } catch (NumberFormatException e) {
//
//            	e.printStackTrace();
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID");
//            }
//        }
//		
//        response.sendRedirect(request.getContextPath() + "/tasks");
//
//		
//
//	}
//	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pageParam = request.getParameter("page");
		int pageNumber = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
		int pageSize = 10;

		Optional<List<Task>> optionalTasks = taskServiceImpl.displayAll(pageNumber, pageSize);
		request.setAttribute("tasks", optionalTasks.get());
		request.setAttribute("currentPage", pageNumber);
		request.setAttribute("totalTasks", taskServiceImpl.getTotalTasks());

		request.getRequestDispatcher("/tasks/DisplayTasks.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

		response.sendRedirect(request.getContextPath() + "/tasks");
	}

	private void addTask(HttpServletRequest request) throws Exception {
		try {
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String priorityStr = request.getParameter("priority");
			String statusStr = request.getParameter("status");
			String dueDate = request.getParameter("dueDate");
			String creationDate = request.getParameter("creationDate");

			validateTaskInput(title, description, priorityStr, statusStr, dueDate, creationDate);

			TaskPriority priority = TaskPriority.valueOf(priorityStr.toUpperCase());
			TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase());
			LocalDate parsedCreationDate = LocalDate.parse(creationDate);
			LocalDate parsedDueDate = LocalDate.parse(dueDate);

			Member member = new Member();
			member.setId(1); // Remplacez par la logique réelle pour obtenir l'utilisateur connecté

			Project project = new Project();
			project.setId(1); // Remplacez par la logique réelle pour obtenir le projet concerné

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
		try {
			task.setId(taskId);
			task.setTitle(title);
			task.setDescription(description);
			task.setPriority(TaskPriority.valueOf(priorityStr.toUpperCase()));

			task.setStatus(TaskStatus.fromString(statusStr));
			task.setDueDate(LocalDate.parse(dueDateStr));
			task.setCreationDate(LocalDate.parse(creationDate));
			Member member = new Member();
			member.setId(1);
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
