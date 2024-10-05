import com.dataware.controller.MemberTeamServlet;
import com.dataware.model.Member;
import com.dataware.model.Team;
import com.dataware.service.MemberService;
import com.dataware.service.TeamService;
import com.dataware.service.MemberTeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static org.mockito.Mockito.*;

class MemberTeamServletTest {

    private MemberTeamServlet memberTeamServlet;
    private MemberService memberServiceMock;
    private TeamService teamServiceMock;
    private MemberTeamService memberTeamServiceMock;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private PrintWriter writerMock;

    @BeforeEach
    void setUp() throws IOException, ServletException {
        // Mock the services
        memberServiceMock = Mockito.mock(MemberService.class);
        teamServiceMock = Mockito.mock(TeamService.class);
        memberTeamServiceMock = Mockito.mock(MemberTeamService.class);
        
        // Instantiate the servlet with mocked services
        memberTeamServlet = new MemberTeamServlet(teamServiceMock, memberServiceMock, memberTeamServiceMock); // Use the constructor with dependency injection
        
        // Mock the HttpServletRequest and HttpServletResponse
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        writerMock = mock(PrintWriter.class);
        
        // Ensure getWriter returns the mocked writer
        when(responseMock.getWriter()).thenReturn(writerMock);
    }

    @Test
    void testAddMemberToTeam_Success() throws IOException, ServletException {
        // Mock request parameters for adding a member to a team
        when(requestMock.getParameter("action")).thenReturn("add");
        when(requestMock.getParameter("memberId")).thenReturn("1");
        when(requestMock.getParameter("teamId")).thenReturn("1");

        Member member = new Member(); // Create a mock Member object
        Team team = new Team(); // Create a mock Team object

        when(memberServiceMock.getMemberById(1)).thenReturn(Optional.of(member));
        when(teamServiceMock.getTeamById(1)).thenReturn(Optional.of(team));
        when(memberTeamServiceMock.addMemberToTeam(member, team)).thenReturn(true);

        // Call the POST method on the servlet
        memberTeamServlet.doPost(requestMock, responseMock);

        // Verify that the member was added to the team
        verify(memberTeamServiceMock, times(1)).addMemberToTeam(member, team);
        verify(responseMock, times(1)).sendRedirect(anyString());
    }

    @Test
    void testAddMemberToTeam_Failure() throws IOException, ServletException {
        // Mock request parameters for adding a member to a team
        when(requestMock.getParameter("action")).thenReturn("add");
        when(requestMock.getParameter("memberId")).thenReturn("1");
        when(requestMock.getParameter("teamId")).thenReturn("1");

        Member member = new Member(); // Create a mock Member object
        Team team = new Team(); // Create a mock Team object

        when(memberServiceMock.getMemberById(1)).thenReturn(Optional.of(member));
        when(teamServiceMock.getTeamById(1)).thenReturn(Optional.of(team));
        when(memberTeamServiceMock.addMemberToTeam(member, team)).thenReturn(false);

        // Call the POST method on the servlet
        memberTeamServlet.doPost(requestMock, responseMock);

        // Verify that the writer was called with an error message
        verify(writerMock, times(1)).write("Failed to add member to team.");
    }

    @Test
    void testRemoveMemberFromTeam_Success() throws IOException, ServletException {
        // Mock request parameters for removing a member from a team
        when(requestMock.getParameter("action")).thenReturn("remove");
        when(requestMock.getParameter("memberId")).thenReturn("1");
        when(requestMock.getParameter("teamId")).thenReturn("1");

        Member member = new Member(); // Create a mock Member object
        Team team = new Team(); // Create a mock Team object

        when(memberServiceMock.getMemberById(1)).thenReturn(Optional.of(member));
        when(teamServiceMock.getTeamById(1)).thenReturn(Optional.of(team));
        when(memberTeamServiceMock.removeMemberFromTeam(member, team)).thenReturn(true);

        // Call the POST method on the servlet
        memberTeamServlet.doPost(requestMock, responseMock);

        // Verify that the member was removed from the team
        verify(memberTeamServiceMock, times(1)).removeMemberFromTeam(member, team);
        verify(responseMock, times(1)).sendRedirect(anyString());
    }

    @Test
    void testRemoveMemberFromTeam_Failure() throws IOException, ServletException {
        // Mock request parameters for removing a member from a team
        when(requestMock.getParameter("action")).thenReturn("remove");
        when(requestMock.getParameter("memberId")).thenReturn("1");
        when(requestMock.getParameter("teamId")).thenReturn("1");

        Member member = new Member(); // Create a mock Member object
        Team team = new Team(); // Create a mock Team object

        when(memberServiceMock.getMemberById(1)).thenReturn(Optional.of(member));
        when(teamServiceMock.getTeamById(1)).thenReturn(Optional.of(team));
        when(memberTeamServiceMock.removeMemberFromTeam(member, team)).thenReturn(false);

        // Call the POST method on the servlet
        memberTeamServlet.doPost(requestMock, responseMock);

        // Verify that the writer was called with an error message
        verify(writerMock, times(1)).write("Failed to remove member from team.");
    }


}
