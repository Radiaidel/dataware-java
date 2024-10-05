<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="com.dataware.model.Task"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Project Management Application</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
	<!-- Bootstrap Navbar -->
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<a class="navbar-brand" href="#">Project Management</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarNav" aria-controls="navbarNav"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

	</nav>
	<!-- Back Button -->
	<div class="text-center mt-4">
		<a href="${pageContext.request.contextPath}/projects?action=list"
			class="btn btn-outline-primary"> <i class="bi bi-arrow-left me-2"></i>Back
			to Project List
		</a>
	</div>






	<div class="container mt-5">
		<!-- Add Team Modal -->
		<!-- Add Team Modal -->


		<!-- Teams Section -->
		<div class="card shadow-lg mt-4">
			<div class="card-header bg-secondary text-white">
				<h4>Teams Working on this Project</h4>
			</div>



			<div class="card-body">
				<c:choose>
					<c:when test="${fn:length(teams) > 0}">
						<ul class="list-group">
							<c:forEach var="team" items="${teams}">
								<li
									class="list-group-item d-flex justify-content-between align-items-center">
									<div>
										<i class="bi bi-people-fill me-2"></i>
										<c:out value="${team.name}" />
									</div>
									<div>
										<!-- Button to remove the team -->
										<form
											action="${pageContext.request.contextPath}/projects?action=removeTeam"
											method="post" style="display: inline;">
											<input type="hidden" name="projectId" value="${project.id}" />
											<input type="hidden" name="teamId" value="${team.id}" />
											<button type="submit" class="btn btn-danger btn-sm">
												<i class="bi bi-trash"></i> Remove
											</button>
										</form>
									</div>
								</li>
							</c:forEach>
						</ul>
					</c:when>
					<c:otherwise>
						<p class="text-danger">This project has no team assigned.</p>
						<button type="button" class="btn btn-success"
							data-bs-toggle="modal" data-bs-target="#addTeamModal">
							<i class="bi bi-plus-circle me-2"></i>Add Team
						</button>
					</c:otherwise>
				</c:choose>
			</div>

			<!-- Add Team Modal -->
			<div class="modal fade" id="addTeamModal" tabindex="-1"
				aria-labelledby="addTeamModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="addTeamModalLabel">Add Team to
								Project</h5>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Close"></button>
						</div>
						<form
							action="${pageContext.request.contextPath}/projects?action=addToProject"
							method="post">
							<div class="modal-body">
								<input type="hidden" name="projectId" value="${project.id}" />
								<div class="mb-3">
									<label for="teamSelect" class="form-label">Select Team
										(Optional)</label> <select id="teamSelect" name="teamId"
										class="form-select" disabled>
										<option value="" selected>Select a team (Optional)</option>
										<!-- Default option -->
										<c:forEach var="team" items="${availableTeams}">
											<option value="${team.id}">${team.name}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary"
									data-bs-dismiss="modal">Close</button>
								<button type="submit" class="btn btn-primary">Add Team</button>
							</div>
						</form>
					</div>
				</div>
			</div>

			<!-- Back Button -->



		</div>


		<hr>

		<div class="text-right mb-3">
			<button type="button" class="btn btn-success" data-toggle="modal"
				data-target="#taskModal">Add New Task</button>
		</div>

		<c:if test="${not empty message}">
			<p>${message}</p>
		</c:if>

		<table class="table table-bordered table-hover">
			<thead class="thead-dark">
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
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="task" items="${tasks}">
					<tr>
						<td><c:out value="${task.id}" /></td>
						<td><c:out value="${task.title}" /></td>
						<td><c:out value="${task.description}" /></td>
						<td><c:out value="${task.priority}" /></td>
						<td><c:out value="${task.status}" /></td>
						<td><c:out value="${task.creationDate}" /></td>
						<td><c:out value="${task.dueDate}" /></td>
						<td><c:out value="${task.project.name}" /></td>
						<td><c:out
								value="${task.member.firstName} ${task.member.lastName}" /></td>
						<td>
							<button class="btn btn-warning btn-sm"
								onclick="editTask('${task.title}', '${task.description}', '${task.priority}', '${task.status}', '${task.dueDate}', '${task.creationDate}', '${task.id}', '${task.member.id}')">
								Edit</button>
							<form action="${pageContext.request.contextPath}/tasks"
								method="post" style="display: inline;">
								<input type="hidden" name="action" value="deletetask"> <input
									type="hidden" name="taskId" value="${task.id}"> <input
									type="hidden" name="projectId" value="${project.id}">

								<button type="submit"
									onclick="return confirm('Are you sure you want to delete this task?');"
									class="btn btn-danger btn-sm">Delete</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<!-- Pagination -->
		<div class="row justify-content-center mt-4">
			<nav aria-label="Page navigation">
				<ul class="pagination">
					<li
						class="page-item <c:if test="${currentPage == 1}">disabled</c:if>">
						<a class="page-link" href="?page=${currentPage - 1}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a>
					</li>
					<c:forEach var="i" begin="1" end="${totalPages}">
						<li
							class="page-item <c:if test="${i == currentPage}">active</c:if>">
							<a class="page-link" href="?page=${i}">${i}</a>
						</li>
					</c:forEach>
					<li
						class="page-item <c:if test="${currentPage == totalPages}">disabled</c:if>">
						<a class="page-link" href="?page=${currentPage + 1}"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a>
					</li>
				</ul>
			</nav>
		</div>
	</div>

	<!-- Modal for Adding Task -->
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
						<input type="hidden" name="action" value="addtask"> <input
							type="hidden" name="projectId" value="${project.id}">
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
						<div class="form-group">
							<label for="assignedMember">Assign to Member</label> <select
								class="form-control" id="assignedMember" name="assignedMember"
								required>
								<c:choose>
									<c:when test="${not empty members}">
										<c:forEach var="member" items="${members}">
											<option value="${member.id}">${member.firstName}
												${member.lastName}</option>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<option disabled>No members available</option>
									</c:otherwise>
								</c:choose>
							</select>
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
					<form id="editTaskForm"
						action="${pageContext.request.contextPath}/tasks" method="post">
						<input type="hidden" name="action" value="updatetask"> <input
							type="hidden" name="taskId" id="editTaskId"> <input
							type="hidden" name="projectId" value="${project.id}">
						<div class="form-group">
							<label for="editTaskTitle">Title</label> <input type="text"
								class="form-control" id="editTaskTitle" name="title" required>
						</div>
						<div class="form-group">
							<label for="editTaskDescription">Description</label>
							<textarea class="form-control" id="editTaskDescription"
								name="description"></textarea>
						</div>
						<div class="form-group">
							<label for="editTaskPriority">Priority</label> <select
								class="form-control" id="editTaskPriority" name="priority"
								required>
								<option value="LOW">Low</option>
								<option value="MEDIUM">Medium</option>
								<option value="HIGH">High</option>
							</select>
						</div>


						<div class="form-group">
							<label for="editTaskStatus">Status</label> <select
								class="form-control" id="editTaskStatus" name="status" required>
								<option value="TO_DO">To Do</option>
								<option value="DOING">Doing</option>
								<option value="DONE">Done</option>
							</select>
						</div>


						<div class="form-group">
							<label for="editTaskCreationDate">Creation Date</label> <input
								type="date" class="form-control" id="editTaskCreationDate"
								name="creationDate" required>
						</div>
						<div class="form-group">
							<label for="editTaskDueDate">Due Date</label> <input type="date"
								class="form-control" id="editTaskDueDate" name="dueDate"
								required>
						</div>
						<div class="form-group">
							<label for="assignedMember">Assign to Member</label> <select
								class="form-control" id="editAssignedMember"
								name="assignedMember" required>
								<c:forEach var="member" items="${members}">
									<option value="${member.id}">${member.firstName}
										${member.lastName}</option>
								</c:forEach>
							</select>

						</div>

						<div class="modal-footer">
							<button type="button" class="btn btn-secondary"
								data-dismiss="modal">Cancel</button>
							<button type="submit" class="btn btn-success">Update
								Task</button>
						</div>
					</form>
				</div>
			</div>
		</div>
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

		function editTask(title, description, priority, status, dueDate,
				creationDate, taskId, memberId) {

			document.getElementById('editTaskTitle').value = title;
			document.getElementById('editTaskDescription').value = description;
			document.getElementById("editTaskPriority").value = priority;
			document.getElementById("editTaskStatus").value = status;
			document.getElementById('editTaskDueDate').value = dueDate;
			document.getElementById('editTaskCreationDate').value = creationDate;
			document.getElementById('editTaskId').value = taskId;

			document.getElementById('assignedMember').value = memberId;

			$('#editTaskModal').modal('show');

		}
	</script>

</body>
</html>
