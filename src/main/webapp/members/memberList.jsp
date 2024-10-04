<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Member List</title>
</head>
<body>
<div class="container mt-5">
    <h1 class="text-center mb-4">Member List</h1>

    <!-- Search Form -->
    <form action="${pageContext.request.contextPath}/members/member" method="get" class="mb-4">
        <input type="hidden" name="action" value="search">
        <div class="input-group">
            <input type="text" name="email" class="form-control" placeholder="Search member by email" required>
            <button class="btn btn-primary" type="submit">Search</button>
        </div>
    </form>
    <a href="${pageContext.request.contextPath}/members/member?action=list" class="btn btn-secondary mb-3">Show All Members</a>

    <!-- Display message if no results are found -->
    <c:if test="${not empty message}">
        <div class="alert alert-info">${message}</div>
    </c:if>

    <!-- Member list table -->
    <table class="table table-striped table-hover">
        <thead>
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="member" items="${members}">
                <tr>
                    <td>${member.id}</td>
                    <td>${member.firstName}</td>
                    <td>${member.lastName}</td>
                    <td>${member.email}</td>
                    <td>${member.role}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/members/member?action=view&id=${member.id}" class="btn btn-info btn-sm">View</a>
                        <a href="${pageContext.request.contextPath}/members/member?action=update&id=${member.id}" 
                           class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#editModal" 
                           data-id="${member.id}" data-firstname="${member.firstName}" data-lastname="${member.lastName}" 
                           data-email="${member.email}" data-role="${member.role}">Edit</a>

                        <form action="${pageContext.request.contextPath}/members/member" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${member.id}">
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this member?');">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- Pagination controls -->
    <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <li class="page-item ${currentPage > 1 ? '' : 'disabled'}">
                <a class="page-link" href="${pageContext.request.contextPath}/members/member?action=list&page=${previousPage}">Previous</a>
            </li>
            <li class="page-item ${currentPage < lastPage ? '' : 'disabled'}">
                <a class="page-link" href="${pageContext.request.contextPath}/members/member?action=list&page=${nextPage}">Next</a>
            </li>
        </ul>
    </nav>

    <!-- Create new member button and modal -->
    <button type="button" class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#createModal">Create New Member</button>

    <!-- Create Member Modal -->
    <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createModalLabel">Create New Member</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="${pageContext.request.contextPath}/members/member?action=create" method="post">
                        <input type="hidden" name="action" value="create">
                        <div class="mb-3">
                            <label for="firstName" class="form-label">First Name:</label>
                            <input type="text" name="first_name" id="firstName" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="lastName" class="form-label">Last Name:</label>
                            <input type="text" name="last_name" id="lastName" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email:</label>
                            <input type="email" name="email" id="email" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="role" class="form-label">Role:</label>
                            <select name="role" id="role" class="form-select">
                                <option value="PROJECT_MANAGER">Project Manager</option>
                                <option value="DEVELOPER">Developer</option>
                                <option value="DESIGNER">Designer</option>
                            </select>
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

    <!-- Edit Member Modal -->
    <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editModalLabel">Edit Member</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="${pageContext.request.contextPath}/members/member?action=update" method="post">
                        <input type="hidden" name="id" id="editMemberId">
                        <div class="mb-3">
                            <label for="editFirstName" class="form-label">First Name:</label>
                            <input type="text" name="first_name" id="editFirstName" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="editLastName" class="form-label">Last Name:</label>
                            <input type="text" name="last_name" id="editLastName" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="editEmail" class="form-label">Email:</label>
                            <input type="email" name="email" id="editEmail" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="editRole" class="form-label">Role:</label>
                            <select name="role" id="editRole" class="form-select">
                                <option value="PROJECT_MANAGER">Project Manager</option>
                                <option value="DEVELOPER">Developer</option>
                                <option value="DESIGNER">Designer</option>
                            </select>
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

</div>

<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
