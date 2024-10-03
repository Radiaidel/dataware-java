<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Team Details</title>
</head>
<body>
<h1>Team Details</h1>

<p><strong>ID:</strong> ${team.id}</p>
<p><strong>Name:</strong> ${team.name}</p>

<a href="${pageContext.request.contextPath}/teams/team?action=list">Back to Team List</a>
</body>
</html>
