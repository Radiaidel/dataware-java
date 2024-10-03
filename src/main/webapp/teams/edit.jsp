<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Team</title>
</head>
<body>
<h1>Edit Team</h1>

<!-- Form to edit an existing team -->
<form action="${pageContext.request.contextPath}/teams/team" method="post">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="${team.id}">
    <label for="name">Team Name:</label>
    <input type="text" name="name" id="name" value="${team.name}" required>
    <button type="submit">Update</button>
</form>

<a href="${pageContext.request.contextPath}/teams/team?action=list">Back to Team List</a>
</body>
</html>
