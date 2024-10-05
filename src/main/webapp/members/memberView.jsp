<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Member Details</title>
</head>
<body>
<h1>Member Details</h1>

<p><strong>ID:</strong> ${member.id}</p>
<p><strong>Name:</strong> ${member.firstName}</p>

<a href="${pageContext.request.contextPath}/members/member?action=list">Back to Team List</a>
</body>
</html>
