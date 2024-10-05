package com.dataware;

import com.dataware.controller.TaskControllerServlet;
import com.dataware.model.Task;
import com.dataware.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class TaskControllerServletTest {

    private TaskControllerServlet taskControllerServlet;
    private TaskServiceImpl taskServiceMock;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;

    @BeforeEach
    void setUp() throws ServletException {

    	taskServiceMock = Mockito.mock(TaskServiceImpl.class);

        // Instantiate the servlet
        taskControllerServlet = new TaskControllerServlet();
        taskControllerServlet.init(); // Call init to set up the taskServiceImpl

        // Replace the real taskServiceImpl with our mock
        taskControllerServlet.setTaskServiceImpl(taskServiceMock);

        // Mock the HttpServletRequest and HttpServletResponse
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
    }

    @Test
    void testAddTask() throws IOException, ServletException {
        // Mock request parameters for a new task creation
        when(requestMock.getParameter("action")).thenReturn("addtask");
        when(requestMock.getParameter("title")).thenReturn("New Task");
        when(requestMock.getParameter("description")).thenReturn("Task Description");
        when(requestMock.getParameter("priority")).thenReturn("HIGH");
        when(requestMock.getParameter("status")).thenReturn("TO_DO");
        when(requestMock.getParameter("dueDate")).thenReturn("2023-12-31");
        when(requestMock.getParameter("creationDate")).thenReturn("2023-05-15");
        when(requestMock.getParameter("assignedMember")).thenReturn("1");
        when(requestMock.getParameter("projectId")).thenReturn("1");

        taskControllerServlet.doPost(requestMock, responseMock);

        // Verify that the taskServiceImpl's addTask method was called once
        try {
			verify(taskServiceMock, times(1)).addTask(any(Task.class));
		} catch (Exception e) {
			e.printStackTrace();
		}

        // Verify the response redirection
        verify(responseMock, times(1)).sendRedirect(contains("/projects?action=details&id=1"));
    }
    
    @Test
    void testDisplayTasks() throws IOException, ServletException {
        // Mock request parameters for displaying tasks
        when(requestMock.getParameter("id")).thenReturn("1");
        when(requestMock.getParameter("page")).thenReturn("1");

        // Create a list of mock tasks to return from the service
        List<Task> mockTasks = new ArrayList<>();
        Task mockTask1 = new Task();
        mockTask1.setTitle("Task 1");
        mockTask1.setDescription("Description 1");
        mockTasks.add(mockTask1);

        // Mock the service method to return the tasks
        when(taskServiceMock.getTasksByProjectId(anyInt(), anyInt(), anyInt())).thenReturn(Optional.of(mockTasks));
        when(taskServiceMock.getTotalTasks()).thenReturn(mockTasks.size());

        RequestDispatcher dispatcherMock = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher("/tasks/DisplayTasks.jsp")).thenReturn(dispatcherMock);

        taskControllerServlet.doGet(requestMock, responseMock);

        // Verify that the tasks were set in the request
        verify(requestMock, times(1)).setAttribute("tasks", mockTasks);
        verify(requestMock, times(1)).setAttribute("currentPage", 1);
        verify(requestMock, times(1)).setAttribute("totalTasks", mockTasks.size());

        // Verify that the request was forwarded to the JSP page
        verify(dispatcherMock, times(1)).forward(requestMock, responseMock);
    }


}