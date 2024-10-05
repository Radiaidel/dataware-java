import com.dataware.controller.TeamServlet;
import com.dataware.model.Member;
import com.dataware.model.Team;
import com.dataware.service.TeamService;
import com.dataware.service.MemberService;
import com.dataware.service.MemberTeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class TeamServletTest {

    private TeamServlet teamServlet;
    private TeamService teamServiceMock;
    private MemberService memberServiceMock;
    private MemberTeamService memberTeamServiceMock;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher requestDispatcherMock;

    @BeforeEach
    void setUp() throws ServletException {
        // Mock the services
        teamServiceMock = Mockito.mock(TeamService.class);
        memberServiceMock = Mockito.mock(MemberService.class);
        memberTeamServiceMock = Mockito.mock(MemberTeamService.class);
        
        // Instantiate the servlet with mocked services
        teamServlet = new TeamServlet(teamServiceMock, memberServiceMock, memberTeamServiceMock); // Use the constructor with dependency injection
        
        // Mock the HttpServletRequest, HttpServletResponse, and RequestDispatcher
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        requestDispatcherMock = mock(RequestDispatcher.class);
        
        // Ensure getRequestDispatcher returns the mocked requestDispatcher
        when(requestMock.getRequestDispatcher("/teams/teamList.jsp")).thenReturn(requestDispatcherMock);
    }


    @Test
    void testCreateTeam() throws IOException, ServletException {
        // Mock request parameters for creating a new team
        when(requestMock.getParameter("action")).thenReturn("create");
        when(requestMock.getParameter("name")).thenReturn("Development");

        // Call the POST method on the servlet
        teamServlet.doPost(requestMock, responseMock);

        // Verify that the teamService's addTeam method was called
        verify(teamServiceMock, times(1)).addTeam(any(Team.class));

        // Verify redirection after creation
        verify(responseMock, times(1)).sendRedirect("team?action=list");
    }



    @Test
    void testUpdateTeam() throws IOException, ServletException {
        // Mock request parameters for updating a team
        when(requestMock.getParameter("action")).thenReturn("update");
        when(requestMock.getParameter("id")).thenReturn("1");
        when(requestMock.getParameter("name")).thenReturn("Updated Team");

        // Call the POST method on the servlet
        teamServlet.doPost(requestMock, responseMock);

        // Verify that the teamService's updateTeam method was called
        verify(teamServiceMock, times(1)).updateTeam(any(Team.class));

        // Verify redirection after updating
        verify(responseMock, times(1)).sendRedirect("team?action=list");
    }


    @Test
    void testDeleteTeam() throws IOException, ServletException {
        // Mock request parameters for deleting a team
        when(requestMock.getParameter("action")).thenReturn("delete");
        when(requestMock.getParameter("id")).thenReturn("1");

        // Call the POST method on the servlet
        teamServlet.doPost(requestMock, responseMock);

        // Verify that the teamService's deleteTeamById method was called
        verify(teamServiceMock, times(1)).deleteTeamById(1);

        // Verify redirection after deletion
        verify(responseMock, times(1)).sendRedirect("team?action=list");
    }

    

 
}
