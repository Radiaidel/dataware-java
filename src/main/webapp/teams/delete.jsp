<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Delete Team</title>
</head>
<body>
<h1>Delete Team</h1>

<p>Are you sure you want to delete the team <strong>${team.name}</strong>?</p>

<!-- Form to confirm deletion -->
<form action="${pageContext.request.contextPath}/teams/team" method="post">
    <input type="hidden" name="action" value="delete">
    <input type="hidden" name="id" value="${team.id}">
    <button type="submit">Delete</button>
</form>

<a href="${pageContext.request.contextPath}/teams/team?action=list">Cancel</a>
</body>
</html>
