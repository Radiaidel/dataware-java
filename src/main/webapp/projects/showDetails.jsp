<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Project Details</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
                        <p><c:out value="${project.name}" /></p>
                    </div>
                    <div class="col-md-6">
                        <h4 class="text-info">Status:</h4>
                        <p><c:out value="${project.status}" /></p>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <h5>Description:</h5>
                        <p><c:out value="${project.description}" /></p>
                    </div>
                    <div class="col-md-6">
                        <h5>Timeline:</h5>
                        <p><strong>Start Date:</strong> <c:out value="${project.startDate}" /></p>
                        <p><strong>End Date:</strong> <c:out value="${project.endDate}" /></p>
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
					                <li class="list-group-item">
					                    <i class="bi bi-people-fill me-2"></i><c:out value="${team.name}" />
					                </li>
					            </c:forEach>
					        </ul>
					    </c:when>
					    <c:otherwise>
					        <p class="text-danger">This project has no team assigned.</p>
					        <a href="${pageContext.request.contextPath}/teams?action=add&projectId=${project.id}" class="btn btn-success">
					            <i class="bi bi-plus-circle me-2"></i>Add Team
					        </a>
					    </c:otherwise>
					</c:choose>
            </div>
        </div>

        <!-- Back Button -->
        <div class="text-center mt-4">
            <a href="${pageContext.request.contextPath}/projects?action=list" class="btn btn-outline-primary">
                <i class="bi bi-arrow-left me-2"></i>Back to Project List
            </a>
        </div>
    </div>

    <!-- Bootstrap JS and Icons -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.js"></script>
</body>
</html>
