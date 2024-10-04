<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Project Management Application</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <!-- Bootstrap Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="#">Project Management</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="<%=request.getContextPath()%>/projects">Projects</a>
                </li>
            </ul>

            <!-- Search Bar -->
            <form class="form-inline my-2 my-lg-0" action="<%=request.getContextPath()%>/projects" method="get">
                <input type="hidden" name="action" value="search">
                <input class="form-control mr-sm-2" type="search" placeholder="Search Projects" aria-label="Search" name="query" required>
                <button class="btn btn-outline-light my-2 my-sm-0" type="submit">Search</button>
            </form>
        </div>
    </nav>

<div class="container mt-5">
    <h3 class="text-center mt-4">List of Projects</h3>
    <hr>

   
    <div class="text-right mb-3">
        <button type="button" class="btn btn-success" data-toggle="modal" data-target="#addProjectModal">
            Add New Project
        </button>
    </div>

   
    <c:if test="${not empty message}">
        <p>${message}</p>
    </c:if>

    
    <table class="table table-bordered table-hover">
        <thead class="thead-dark">
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="project" items="${projects}">
                <tr>
                    <td><c:out value="${project.name}" /></td>
                    <td><c:out value="${project.description}" /></td>
                    <td><c:out value="${project.startDate}" /></td>
                    <td><c:out value="${project.endDate}" /></td>
                    <td><c:out value="${project.status}" /></td>
                    <td>
                       
                        <form action="${pageContext.request.contextPath}/projects" method="get" style="display:inline;">
						    <input type="hidden" name="action" value="details">
						    <input type="hidden" name="id" value="${project.id}">
						    <button type="submit" class="btn btn-info btn-sm">View Details</button>
					</form>
                        <button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#editProjectModal"
                                data-id="<c:out value='${project.id}' />"
                                data-name="<c:out value='${project.name}' />"
                                data-description="<c:out value='${project.description}' />"
                                data-startdate="<c:out value='${project.startDate}' />"
                                data-enddate="<c:out value='${project.endDate}' />"
                                data-status="<c:out value='${project.status}' />">
                            Edit
                        </button>
                        <form action="${pageContext.request.contextPath}/projects" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${project.id}">
                            <button type="submit" onclick="return confirm('Are you sure you want to delete this project?');" class="btn btn-danger btn-sm">Delete</button>
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
                <li class="page-item <c:if test="${currentPage == 1}">disabled</c:if>">
                    <a class="page-link" href="?page=${previousPage}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <c:forEach var="i" begin="1" end="${lastPage}">
                    <li class="page-item <c:if test="${i == currentPage}">active</c:if>">
                        <a class="page-link" href="?page=${i}">${i}</a>
                    </li>
                </c:forEach>
                <li class="page-item <c:if test="${currentPage == lastPage}">disabled</c:if>">
                    <a class="page-link" href="?page=${nextPage}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>
</div>


    <!-- Add Project Modal -->
    <div class="modal fade" id="addProjectModal" tabindex="-1" role="dialog" aria-labelledby="addProjectModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addProjectModalLabel">Add New Project</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- Add Project Form -->
                    <form action="<%=request.getContextPath()%>/projects?action=insert" method="post">
                        <div class="form-group">
                            <label for="projectName">Project Name</label>
                            <input type="hidden" name="action" value="insert">
                            <input type="text" class="form-control" id="projectName" name="name" required>
                        </div>
                        <div class="form-group">
                            <label for="projectDescription">Description</label>
                            <textarea class="form-control" id="projectDescription" name="description" required></textarea>
                        </div>
                        <div class="form-group">
                            <label for="startDate">Start Date</label>
                            <input type="date" class="form-control" id="startDate" name="start_date" required>
                        </div>
                        <div class="form-group">
                            <label for="endDate">End Date</label>
                            <input type="date" class="form-control" id="endDate" name="end_date" required>
                        </div>
                        <div class="form-group">
                            <label for="status">Status</label>
                            <select class="form-control" id="status" name="status" required>
                                <option value="In_preparation">En Préparation</option>
                                <option value="In_progress">En Cours</option>
                                <option value="Paused">En Pause</option>
                                <option value="Completed">Terminé</option>
                                <option value="Canceled">Annulé</option>
                            </select>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Add Project</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Project Modal -->
    <div class="modal fade" id="editProjectModal" tabindex="-1" role="dialog" aria-labelledby="editProjectModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editProjectModalLabel">Edit Project</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- Edit Project Form -->
                    <form action="<%=request.getContextPath()%>/projects?action=update" method="post">
                        <input type="hidden" id="editProjectId" name="id">
                        <div class="form-group">
                            <label for="editProjectName">Project Name</label>
                            <input type="text" class="form-control" id="editProjectName" name="name" required>
                        </div>
                        <div class="form-group">
                            <label for="editProjectDescription">Description</label>
                            <textarea class="form-control" id="editProjectDescription" name="description" required></textarea>
                        </div>
                        <div class="form-group">
                            <label for="editStartDate">Start Date</label>
                            <input type="date" class="form-control" id="editStartDate" name="start_date" required>
                        </div>
                        <div class="form-group">
                            <label for="editEndDate">End Date</label>
                            <input type="date" class="form-control" id="editEndDate" name="end_date" required>
                        </div>
                        <div class="form-group">
                            <label for="editStatus">Status</label>
                            <select class="form-control" id="editStatus" name="status" required>
                                <option value="In_preparation">En Préparation</option>
                                <option value="In_progress">En Cours</option>
                                <option value="Paused">En Pause</option>
                                <option value="Completed">Terminé</option>
                                <option value="Canceled">Annulé</option>
                            </select>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Update Project</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        // Script to handle edit button functionality
        $('#editProjectModal').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget); // Button that triggered the modal
            var id = button.data('id');
            var name = button.data('name');
            var description = button.data('description');
            var startDate = button.data('startdate');
            var endDate = button.data('enddate');
            var status = button.data('status');

            var modal = $(this);
            modal.find('#editProjectId').val(id);
            modal.find('#editProjectName').val(name);
            modal.find('#editProjectDescription').val(description);
            modal.find('#editStartDate').val(startDate);
            modal.find('#editEndDate').val(endDate);
            modal.find('#editStatus').val(status);
        });
    </script>
</body>
</html>
