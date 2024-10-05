<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">

<html>
<head>
    <title>Team Details</title>
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center">Team Details</h1>
        <div class="card mb-4">
            <div class="card-body">
                <p><strong>ID:</strong> ${team.id}</p>
                <p><strong>Name:</strong> ${team.name}</p>
            </div>
        </div>

        <!-- Button to open modal for adding members -->

        <a href="${pageContext.request.contextPath}/teams/team?action=list" class="btn btn-link">Back to Team List</a>
        
        <div class="mt-4 flex flex-start">
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createModal">
                Add Members
            </button>
        </div>

        <!-- Display available members -->
        <h2 class="mt-3">Members of the team</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="member" items="${membersoftheTeam}">
                    <tr>
                        <td>${member.id}</td>
                        <td>${member.firstName}</td>
                        <td>${member.lastName}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/member-team?action=remove" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="teamId" value="${team.id}">
                                <input type="hidden" name="memberId" value="${member.id}">
                                <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to remove this member?');">Remove</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Pagination controls -->
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="?action=view&id=${team.id}&page=${previousPage}">Previous</a>
                </li>
                <c:forEach begin="1" end="${lastPage}" var="i">
                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                        <a class="page-link" href="?action=view&id=${team.id}&page=${i}">${i}</a>
                    </li>
                </c:forEach>
                <li class="page-item ${currentPage == lastPage ? 'disabled' : ''}">
                    <a class="page-link" href="?action=view&id=${team.id}&page=${nextPage}">Next</a>
                </li>
            </ul>
        </nav>
    </div>

    <!-- Include your modal for adding members -->
    <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createModalLabel">Add Member to Team</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="${pageContext.request.contextPath}/member-team?action=add" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="teamId" value="${team.id}">

                        <div class="mb-3">
                            <label for="memberId" class="form-label">Select Member:</label>
                            <select name="memberId" id="memberId" class="form-select">
                                <option value="">--Select Member--</option>
                                <c:forEach var="member" items="${allMembers}">
                                    <option value="${member.id}">${member.firstName} ${member.lastName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-primary">Add Member</button>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>