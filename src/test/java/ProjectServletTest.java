import com.dataware.controller.ProjectServlet;
import com.dataware.model.Project;
import com.dataware.model.enums.ProjectStatus;
import com.dataware.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProjectServletTest {

    private ProjectServlet projectServlet;

    @Mock
    private ProjectServiceImpl projectServiceImp;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        projectServlet = new ProjectServlet();
        projectServlet.init();
        projectServlet.projectServiceImp = projectServiceImp;
    }

    @Test
    public void testListProject() throws Exception {
        // Arrange
        List<Project> mockProjects = List.of(
                new Project("Project 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(5), ProjectStatus.In_preparation),
                new Project("Project 2", "Description 2", LocalDate.now(), LocalDate.now().plusDays(10), ProjectStatus.Completed)
        );

        when(projectServiceImp.getAllProjects(1, 3)).thenReturn(mockProjects);
        when(projectServiceImp.getProjectCount()).thenReturn(2);
        when(request.getParameter("page")).thenReturn("1");
        when(request.getRequestDispatcher("/projects/list.jsp")).thenReturn(dispatcher);

        // Act
        projectServlet.listProject(request, response);

        // Assert
        verify(request).setAttribute("projects", mockProjects);
        verify(request).setAttribute("currentPage", 1);
        verify(request).setAttribute("totalProjects", 2);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testInsertProject() throws Exception {
        // Arrange
        when(request.getParameter("name")).thenReturn("New Project");
        when(request.getParameter("description")).thenReturn("Description");
        when(request.getParameter("start_date")).thenReturn("2024-01-01");
        when(request.getParameter("end_date")).thenReturn("2024-01-10");
        when(request.getParameter("status")).thenReturn("In_progress");

        projectServlet.insertProject(request, response);

        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectServiceImp).createProject(projectCaptor.capture());
        
        Project createdProject = projectCaptor.getValue();
        assertEquals("New Project", createdProject.getName());
        assertEquals("Description", createdProject.getDescription());
        assertEquals(LocalDate.of(2024, 1, 1), createdProject.getStartDate());
        assertEquals(LocalDate.of(2024, 1, 10), createdProject.getEndDate());
        assertEquals(ProjectStatus.In_progress, createdProject.getStatus()); 

        verify(response).sendRedirect("projects");
    }
}
