<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>Create Project</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        form {
            width: 300px;
            margin: auto;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }
        input[type="text"], input[type="date"], textarea, select {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        .form-title {
            text-align: center;
            margin-bottom: 20px;
        }
    </style>
<title>Create Project</title>
</head>
<body>
<h2 class="form-title">Create a New Project</h2>

<form action="<%= request.getContextPath() %>/projects" method="post">
    <label for="name">Project Name:</label>
    <input type="text" id="name" name="name" placeholder="Enter project name" required>
    
    <label for="description">Description:</label>
    <textarea id="description" name="description" rows="4" placeholder="Enter project description" required></textarea>
    
    <label for="start_date">Start Date:</label>
    <input type="date" id="start_date" name="start_date" required>
    
    <label for="end_date">End Date:</label>
    <input type="date" id="end_date" name="end_date" required>
    
    <label for="status">Status:</label>
    <select id="status" name="status" required>
        <option value="In_preparation">En Préparation</option>
        <option value="In_progress">En Cours</option>
        <option value="Paused">En Pause</option>
        <option value="Completed">Terminé</option>
        <option value="Canceled">Annulé</option>
    </select>
    
    <input type="submit" value="Create Project">
</form>
</body>
</html>