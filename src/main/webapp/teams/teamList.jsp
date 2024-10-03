<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">

<html>
<head>
    <title>Team List</title>
</head>
<body class="container ml-12">
<h1>Team List</h1>

<!-- Search Form -->
<form action="${pageContext.request.contextPath}/teams/team" method="get">
    <input type="hidden" name="action" value="search">
    <input type="text" name="name" placeholder="Search team by name" required>
    <button type="submit">Search</button>
</form>
<a href="${pageContext.request.contextPath}/teams/team?action=list">Show All Teams</a>


<!-- Display message if no results are found -->
<c:if test="${not empty message}">
    <p>${message}</p>
</c:if>

<!-- Team list table -->
<table border="1">
    <thead>
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
                <a href="${pageContext.request.contextPath}/teams/team?action=view&id=${team.id}">View</a> |
                <a href="${pageContext.request.contextPath}/teams/team?action=update&id=${team.id}" 
                type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#staticBackdrop" 
                data-id="${team.id}" data-name="${team.name}">Edit</a> |
                
                <form action="${pageContext.request.contextPath}/teams/team" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${team.id}">
                    <button type="submit" onclick="return confirm('Are you sure you want to delete this team?');">Delete</button>
                </form>
                   
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Pagination controls -->
<p>
    <%-- <c:if test="${currentPage > 1}">
        <a href="${pageContext.request.contextPath}/teams/team?action=list&page=1">First Page</a> |
    </c:if>
     --%>
   <a href="${pageContext.request.contextPath}/teams/team?action=list&page=${previousPage}" 
   class="${currentPage > 1 ? '' : 'disabled'}">Previous</a> |

<a href="${pageContext.request.contextPath}/teams/team?action=list&page=${nextPage}" 
   class="${currentPage < lastPage ? '' : 'disabled'}">Next</a> |

    
   <%--  <c:if test="${currentPage < lastPage}">
        <a href="${pageContext.request.contextPath}/teams/team?action=list&page=${lastPage}">Last Page</a>
    </c:if> --%>
</p>

<!-- Create new team -->
<!-- Button trigger modal -->
<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
  Creer une equipe
</button>

<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Creer une equipe</h5>
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
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
      </div>
    </div>
  </div>
</div>

<!-- ------------------------------------ edit modal ------------------------------------- -->
 <!-- Button trigger modal -->

<!-- Modal -->
<div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="staticBackdropLabel">Modifier equipe</h5>
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
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
      </div>
    </div>
  </div>
</div>
 
<%-- <a href="${pageContext.request.contextPath}/teams/create.jsp">Create New Team</a>
 --%>    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/script.js"></script>

</body>
</html>
