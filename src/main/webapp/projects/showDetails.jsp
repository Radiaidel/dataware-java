<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Project Details</title>
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container mt-5">
		<!-- Project Details Card -->
		<div class="card shadow-lg">
			<div class="card-header text-center bg-primary text-white">
				<h3>Project Details</h3>
			</div>
			<div class="card-body">
				<!-- Project Info -->
				<div class="row mb-4">
					<div class="col-md-6">
						<h4 class="text-info">Project Name:</h4>
						<p>
							<c:out value="${project.name}" />
						</p>
					</div>
					<div class="col-md-6">
						<h4 class="text-info">Status:</h4>
						<p>
							<c:out value="${project.status}" />
						</p>
					</div>
				</div>

				<div class="row">
					<div class="col-md-6">
						<h5>Description:</h5>
						<p>
							<c:out value="${project.description}" />
						</p>
					</div>
					<div class="col-md-6">
						<h5>Timeline:</h5>
						<p>
							<strong>Start Date:</strong>
							<c:out value="${project.startDate}" />
						</p>
						<p>
							<strong>End Date:</strong>
							<c:out value="${project.endDate}" />
						</p>
					</div>
				</div>
			</div>
		</div>

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
		<div class="text-center mt-4">
			<a href="${pageContext.request.contextPath}/projects?action=list"
				class="btn btn-outline-primary "> <i
				class="bi bi-arrow-left me-2"></i>Back to Project List
			</a>
		</div>

		<!-- Bootstrap JS and Icons -->
		<script
			src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
		<script
			src="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.js"></script>

		<script type="text/javascript">
			document.addEventListener('DOMContentLoaded', function() {
				document.getElementById('teamSelect').disabled = false;
			});
		</script>
</body>
</html>
