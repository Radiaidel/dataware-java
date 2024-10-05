<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Team List</title>
</head>
<body class="container mt-4">
    <h1 class="text-center mb-4">Team List</h1>

    <!-- Search Form -->
    <form action="${pageContext.request.contextPath}/teams/team" method="get" class="mb-4">
        <input type="hidden" name="action" value="search">
        <div class="input-group">
            <input type="text" name="name" class="form-control" placeholder="Search team by name" required>
            <button class="btn btn-primary" type="submit">Search</button>
        </div>
    </form>
    <a href="${pageContext.request.contextPath}/teams/team?action=list" class="btn btn-secondary mb-3">Show All Teams</a>

    <!-- Display message if no results are found -->
    <c:if test="${not empty message}">
        <div class="alert alert-info">${message}</div>
    </c:if>

    <!-- Team list table -->
    <table class="table table-bordered table-striped">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="team" items="${teams}">
                <tr>
                    <td>${team.id}</td>
                    <td>${team.name}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/teams/team?action=view&id=${team.id}" class="btn btn-info btn-sm">View</a>
                        <a href="${pageContext.request.contextPath}/teams/team?action=update&id=${team.id}" 
                           class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#staticBackdrop" 
                           data-id="${team.id}" data-name="${team.name}">Edit</a>

                        <form action="${pageContext.request.contextPath}/teams/team" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${team.id}">
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this team?');">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- Pagination controls -->
    <nav aria-label="Page navigation">
        <ul class="pagination">
            <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                <a class="page-link" href="${pageContext.request.contextPath}/teams/team?action=list&page=${previousPage}">Previous</a>
            </li>
            <li class="page-item ${currentPage >= lastPage ? 'disabled' : ''}">
                <a class="page-link" href="${pageContext.request.contextPath}/teams/team?action=list&page=${nextPage}">Next</a>
            </li>
        </ul>
    </nav>

    <!-- Create new team -->
    <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#exampleModal">
        Create a Team
    </button>

    <!-- Create Team Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Create a Team</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="${pageContext.request.contextPath}/teams/team?action=create" method="post">
                        <input type="hidden" name="action" value="create">
                        <div class="mb-3">
                            <label for="name" class="form-label">Team Name:</label>
                            <input type="text" name="name" id="name" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Create</button>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Team Modal -->
    <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="staticBackdropLabel">Edit Team</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="${pageContext.request.contextPath}/teams/team?action=update" method="post">
                        <input type="hidden" name="id" id="editTeamId" required>
                        <div class="mb-3">
                            <label for="editName" class="form-label">Team Name:</label>
                            <input type="text" name="name" id="editName" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Update</button>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS and custom script -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>