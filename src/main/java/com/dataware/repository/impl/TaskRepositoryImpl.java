package com.dataware.repository.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dataware.database.DatabaseConnection;
import com.dataware.model.Member;
import com.dataware.model.Project;
import com.dataware.model.Task;
import com.dataware.model.enums.MemberRole;
import com.dataware.model.enums.ProjectStatus;
import com.dataware.model.enums.TaskPriority;
import com.dataware.model.enums.TaskStatus;
import com.dataware.repository.TaskRepository;

public class TaskRepositoryImpl implements TaskRepository {

	private final Connection conn = DatabaseConnection.getInstance().getConnection();

	@Override
	public boolean addTask(Task task) {

		String query = " INSERT INTO `task`(`title`, `description`, `priority`, `status`, `creation_date`, `due_date`, `project_id`, `member_id`) VALUES (? , ? , ? , ? , ? , ? , ? , ?)";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, task.getTitle());
			pstmt.setString(2, task.getDescription());
			pstmt.setString(3, task.getPriority().toString());
			pstmt.setString(4, task.getStatus().toString());
			pstmt.setDate(5, Date.valueOf(task.getCreationDate()));
			pstmt.setDate(6, Date.valueOf(task.getDueDate()));
			pstmt.setInt(7, task.getProject().getId());
			pstmt.setInt(8, task.getMember().getId());

			return pstmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateTask(Task task) {
		String query = "UPDATE `task` SET `title`=? ,`description`= ? ,`priority`= ? ,`status`= ? ,`creation_date`= ? ,`due_date`= ? ,`project_id`= ? ,`member_id`= ?  WHERE id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, task.getTitle());
			pstmt.setString(2, task.getDescription());
			pstmt.setString(3, task.getPriority().toString());
			pstmt.setString(4, task.getStatus().toString());
			pstmt.setDate(5, Date.valueOf(task.getCreationDate()));
			pstmt.setDate(6, Date.valueOf(task.getDueDate()));
			pstmt.setInt(7, task.getProject().getId());
			pstmt.setInt(8, task.getMember().getId());

			pstmt.setInt(9, task.getId());

			return pstmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteTask(int id) {
		String query = "DELETE FROM `task` WHERE id =?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, id);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Optional<List<Task>> displayAll(int pageNumber, int pageSize) {

		int offset = (pageNumber - 1) * pageSize;

	    String query = "SELECT t.id as `task_id`, t.title, t.description as `task_description`, t.priority, t.status as `task_status`, " +
	                   "t.creation_date, t.due_date, p.id as `project_id`, p.name, p.description as `project_description`, " +
	                   "p.start_date, p.end_date, p.status as `project_status`, m.id as `member_id`, m.first_name, m.last_name, " +
	                   "m.email, m.role " +
	                   "FROM `task` t " +
	                   "JOIN `project` p ON p.id = t.project_id " +
	                   "JOIN `member` m ON m.id = t.member_id " +
	                   "LIMIT ? OFFSET ?;";

	    List<Task> tasks = new ArrayList<>();

	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {

	    	pstmt.setInt(1, pageSize);
	        pstmt.setInt(2, offset);

	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            Project project = new Project();
	            project.setId(rs.getInt("project_id"));
	            project.setName(rs.getString("name"));
	            project.setDescription(rs.getString("project_description"));
	            project.setStartDate(rs.getDate("start_date").toLocalDate());
	            project.setEndDate(rs.getDate("end_date").toLocalDate());
	            project.setStatus(ProjectStatus.valueOf(rs.getString("project_status")));

	            Member member = new Member();
	            member.setId(rs.getInt("member_id"));
	            member.setFirstName(rs.getString("first_name"));
	            member.setLastName(rs.getString("last_name"));
	            member.setEmail(rs.getString("email"));
	            member.setRole(MemberRole.valueOf(rs.getString("role")));

	            Task task = new Task();
	            task.setId(rs.getInt("task_id"));
	            task.setTitle(rs.getString("title"));
	            task.setDescription(rs.getString("task_description"));
	            task.setPriority(TaskPriority.valueOf(rs.getString("priority")));
	            task.setStatus(TaskStatus.valueOf(rs.getString("task_status")));
	            task.setCreationDate(rs.getDate("creation_date").toLocalDate());
	            task.setDueDate(rs.getDate("due_date").toLocalDate());
	            task.setProject(project);
	            task.setMember(member);

	            tasks.add(task);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks);
	}


}
