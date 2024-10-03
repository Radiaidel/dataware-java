<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

		<!-- Modal pour ajouter une tâche -->
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
						<form id="taskForm">
							<div class="form-group">
								<label for="taskTitle">Title</label> <input type="text"
									class="form-control" id="taskTitle" required>
							</div>
							<div class="form-group">
								<label for="taskDescription">Description</label>
								<textarea class="form-control" id="taskDescription"></textarea>
							</div>
							<div class="form-group">
								<label for="taskPriority">Priority</label> <select
									class="form-control" id="taskPriority" required>
									<option value="Low">Low</option>
									<option value="Medium">Medium</option>
									<option value="High">High</option>
								</select>
							</div>
							<div class="form-group">
								<label for="taskStatus">Status</label> <select
									class="form-control" id="taskStatus" required>
									<option value="To Do">To Do</option>
									<option value="Doing">Doing</option>
									<option value="Done">Done</option>
								</select>
							</div>
							<div class="form-group">
								<label for="dueDate">Due Date</label> <input type="date"
									class="form-control" id="dueDate" required>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Cancel</button>
						<button type="button" class="btn btn-success"
							onclick="submitTask()">Submit</button>
					</div>
				</div>
			</div>
		</div>

		<!-- Modal pour modifier une tâche -->
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

		<!-- Liste des tâches -->
		<div class="task-card ">
			<div class="task-details">
				<h5>Task 1: Design Mockups</h5>
				<p>Description: Design wireframes for the landing page.</p>
			</div>
			<div class="task-meta">
				<div class="status px-5">
					<span class="badge badge-to-do">To Do</span>
				</div>
				<div class="priority px-5">
					<span class="priority-height priority-low"></span>
				</div>
				<div class="member px-5 ">John Doe</div>
				<button class="btn btn-warning btn-sm ml-3"
					onclick="editTask('Task 1: Design Mockups', 'Design wireframes for the landing page.', 'Low', 'To Do', '2024-10-01')">Edit</button>
			</div>
		</div>

		<div class="task-card">
			<div class="task-details">
				<h5>Task 2: Define APIs</h5>
				<p>Description: Define API endpoints for user management.</p>
			</div>
			<div class="task-meta">
				<div class="status px-5">
					<span class="badge badge-doing">Doing</span>
				</div>
				<div class="priority px-5">
					<span class="priority-height priority-medium"></span> <span
						class="priority-height priority-medium"></span>

				</div>
				<div class="member px-5">Jane Smith</div>
				<button class="btn btn-warning btn-sm ml-3"
					onclick="editTask('Task 2: Define APIs', 'Define API endpoints for user management.', 'Medium', 'Doing', '2024-10-02')">Edit</button>
			</div>
		</div>

		<div class="task-card">
			<div class="task-details">
				<h5>Task 3: Implement Login</h5>
				<p>Description: Implement the login functionality.</p>
			</div>
			<div class="task-meta">
				<div class="status px-5">
					<span class="badge badge-done">Done</span>
				</div>
				<div class="priority px-5">
					<span class="priority-height priority-high"></span> <span
						class="priority-height priority-high"></span> <span
						class="priority-height priority-high"></span>

				</div>
				<div class="member px-5">Alice Johnson</div>
				<button class="btn btn-warning btn-sm ml-3"
					onclick="editTask('Task 3: Implement Login', 'Implement the login functionality.', 'High', 'Done', '2024-09-30')">Edit</button>
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

            // Logique pour ajouter la tâche ici (par exemple, envoyer les données au serveur)
            console.log('New Task:', { title, description, priority, status, dueDate });
            $('#taskModal').modal('hide'); // Ferme la modal
        }

        function editTask(title, description, priority, status, dueDate) {
            document.getElementById('editTaskTitle').value = title;
            document.getElementById('editTaskDescription').value = description;
            document.getElementById('editTaskPriority').value = priority;
            document.getElementById('editTaskStatus').value = status;
            document.getElementById('editDueDate').value = dueDate;

            $('#editTaskModal').modal('show'); // Ouvre la modal de modification
        }

        function updateTask() {
            const title = document.getElementById('editTaskTitle').value;
            const description = document.getElementById('editTaskDescription').value;
            const priority = document.getElementById('editTaskPriority').value;
            const status = document.getElementById('editTaskStatus').value;
            const dueDate = document.getElementById('editDueDate').value;

            // Logique pour mettre à jour la tâche ici (par exemple, envoyer les données au serveur)
            console.log('Updated Task:', { title, description, priority, status, dueDate });
            $('#editTaskModal').modal('hide'); // Ferme la modal
        }
    </script>
	</div>

</body>
</html>
