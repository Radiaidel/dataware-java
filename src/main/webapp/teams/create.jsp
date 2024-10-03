<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Team</title>
</head>
<body>
<h1>Create New Team</h1>

<!-- Form to create a new team -->
<form action="${pageContext.request.contextPath}/teams/team?action=create" method="post">
    <input type="hidden" name="action" value="create">
    <label for="name">Team Name:</label>
    <input type="text" name="name" id="name" required>
    <button type="submit">Create</button>
</form>

<a href="${pageContext.request.contextPath}/teams/team?action=list">Back to Team List</a>
</body>
</html>
