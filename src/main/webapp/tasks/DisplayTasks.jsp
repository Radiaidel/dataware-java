<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="com.dataware.model.Task"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<style>
body {
	background-color: #f4f5f7;
	font-family: Arial, sans-serif;
}

.container {
	margin-top: 20px;
}

.task-card {
	background-color: #ffffff;
	border-radius: 8px;
	box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.1);
	margin-bottom: 15px;
	padding: 15px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.task-details {
	flex: 1;
	margin-right: 20px;
}

.task-details h5 {
	font-size: 18px;
	margin: 0 0 5px 0;
	color: #172b4d;
}

.task-details p {
	font-size: 14px;
	margin: 0 0 5px 0;
	color: #5e6c84;
}

.task-meta {
	display: flex;
	align-items: center;
}

.badge {
	padding: 5px 10px;
	font-size: 12px;
	border-radius: 5px;
}

.badge-to-do {
	background-color: #007bff;
	color: white;
}

.badge-doing {
	background-color: #ffc107;
	color: black;
}

.badge-done {
	background-color: #28a745;
	color: white;
}

.priority {
	display: flex;
	flex-direction: column;
}

.priority span {
	display: block;
	width: 20px;
	margin-bottom: 2px;
	background-color: orange;
}

.priority-height {
	height: 2px;
}
</style>
</head>
<body>

	<div class="container">
		<div class="text-right mb-3">
			<button class="btn btn-primary" data-toggle="modal"
				data-target="#taskModal">Add Task</button>
		</div>

		<div class="modal fade" id="taskModal" tabindex="-1" role="dialog"
			aria-labelledby="taskModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="taskModalLabel">Add New Task</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form id="taskForm"
							action="${pageContext.request.contextPath}/tasks" method="post">
							<input type="hidden" name="action" value="addtask">
							<div class="form-group">
								<label for="taskTitle">Title</label> <input type="text"
									class="form-control" id="taskTitle" name="title" required>
							</div>
							<div class="form-group">
								<label for="taskDescription">Description</label>
								<textarea class="form-control" id="taskDescription"
									name="description"></textarea>
							</div>
							<div class="form-group">
								<label for="taskPriority">Priority</label> <select
									class="form-control" id="taskPriority" name="priority" required>
									<option value="Low">Low</option>
									<option value="Medium">Medium</option>
									<option value="High">High</option>
								</select>
							</div>
							<div class="form-group">
								<label for="taskStatus">Status</label> <select
									class="form-control" id="taskStatus" name="status" required>
									<option value="TO_DO">To Do</option>
									<option value="Doing">Doing</option>
									<option value="Done">Done</option>
								</select>
							</div>
							<div class="form-group">
								<label for="creationDate">Creation Date</label> <input
									type="date" class="form-control" id="creationDate"
									name="creationDate" required>
							</div>
							<div class="form-group">
								<label for="dueDate">Due Date</label> <input type="date"
									class="form-control" id="dueDate" name="dueDate" required>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">Cancel</button>
								<button type="submit" class="btn btn-success">Submit</button>
							</div>
						</form>

					</div>

				</div>
			</div>
		</div>

		<div class="modal fade" id="editTaskModal" tabindex="-1" role="dialog"
			aria-labelledby="editTaskModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="editTaskModalLabel">Edit Task</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form id="editTaskForm">
							<div class="form-group">
								<label for="editTaskTitle">Title</label> <input type="text"
									class="form-control" id="editTaskTitle" required>
							</div>
							<div class="form-group">
								<label for="editTaskDescription">Description</label>
								<textarea class="form-control" id="editTaskDescription"></textarea>
							</div>
							<div class="form-group">
								<label for="editTaskPriority">Priority</label> <select
									class="form-control" id="editTaskPriority" required>
									<option value="Low">Low</option>
									<option value="Medium">Medium</option>
									<option value="High">High</option>
								</select>
							</div>
							<div class="form-group">
								<label for="editTaskStatus">Status</label> <select
									class="form-control" id="editTaskStatus" required>
									<option value="To Do">To Do</option>
									<option value="Doing">Doing</option>
									<option value="Done">Done</option>
								</select>
							</div>
							<div class="form-group">
								<label for="editDueDate">Due Date</label> <input type="date"
									class="form-control" id="editDueDate" required>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Cancel</button>
						<button type="button" class="btn btn-success"
							onclick="updateTask()">Save Changes</button>
					</div>
				</div>
			</div>
		</div>


		<table border="1">
			<tr>
				<th>ID</th>
				<th>Title</th>
				<th>Description</th>
				<th>Priority</th>
				<th>Status</th>
				<th>Creation Date</th>
				<th>Due Date</th>
				<th>Project</th>
				<th>Assigned Member</th>
			</tr>
			<%
			List<Task> tasks = (List<Task>) request.getAttribute("tasks");
			for (Task task : tasks) {
			%>
			<tr>
				<td><%=task.getId()%></td>
				<td><%=task.getTitle()%></td>
				<td><%=task.getDescription()%></td>
				<td><%=task.getPriority()%></td>
				<td><%=task.getStatus()%></td>
				<td><%=task.getCreationDate()%></td>
				<td><%=task.getDueDate()%></td>
				<td><%=task.getProject().getName()%></td>
				<td><%=task.getMember().getFirstName() + " " + task.getMember().getLastName()%></td>
			</tr>
			<%
			}
			%>
		</table>

		<%
		// Pagination logic
		int currentPage = (Integer) request.getAttribute("currentPage");
		int totalTasks = (Integer) request.getAttribute("totalTasks");  // Méthode à créer pour obtenir le nombre total de tâches
		int totalPages = (int) Math.ceil((double) totalTasks / 10); // Supposant que pageSize = 10

		if (currentPage > 1) {
		%>
		<a href="tasks?page=<%=currentPage - 1%>">Previous</a>
		<%
		}
		if (currentPage < totalPages) {
		%>
		<a href="tasks?page=<%=currentPage + 1%>">Next</a>
		<%
		}
		%>


	</div>




	<!-- Scripts -->
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

	<script>
		function submitTask() {
			const title = document.getElementById('taskTitle').value;
			const description = document.getElementById('taskDescription').value;
			const priority = document.getElementById('taskPriority').value;
			const status = document.getElementById('taskStatus').value;
			const dueDate = document.getElementById('dueDate').value;

			$('#taskModal').modal('hide');
		}

		function editTask(title, description, priority, status, dueDate) {
			document.getElementById('editTaskTitle').value = title;
			document.getElementById('editTaskDescription').value = description;
			document.getElementById('editTaskPriority').value = priority;
			document.getElementById('editTaskStatus').value = status;
			document.getElementById('editDueDate').value = dueDate;

			$('#editTaskModal').modal('show');
		}

		function updateTask() {
			const title = document.getElementById('editTaskTitle').value;
			const description = document.getElementById('editTaskDescription').value;
			const priority = document.getElementById('editTaskPriority').value;
			const status = document.getElementById('editTaskStatus').value;
			const dueDate = document.getElementById('editDueDate').value;

			$('#editTaskModal').modal('hide');
		}
	</script>
	</div>

</body>
</html>
