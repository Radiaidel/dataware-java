import com.dataware.controller.MemberServlet;
import com.dataware.model.Member;
import com.dataware.model.enums.MemberRole;
import com.dataware.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServletTest {

    private MemberServlet memberServlet;
    private MemberService memberServiceMock;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher requestDispatcherMock;

    @BeforeEach
    void setUp() throws ServletException {
        // Mock the MemberService
        memberServiceMock = Mockito.mock(MemberService.class);
        
        // Instantiate the servlet with the mocked service
        memberServlet = new MemberServlet(memberServiceMock);  // Use the constructor with dependency injection

        // Mock the HttpServletRequest, HttpServletResponse, and RequestDispatcher
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        requestDispatcherMock = mock(RequestDispatcher.class);
    }

    @Test
    void testCreateMember() throws IOException, ServletException {
        // Mock request parameters for a new member creation
        when(requestMock.getParameter("action")).thenReturn("create");
        when(requestMock.getParameter("first_name")).thenReturn("John");
        when(requestMock.getParameter("last_name")).thenReturn("Doe");
        when(requestMock.getParameter("email")).thenReturn("john.doe@example.com");
        when(requestMock.getParameter("role")).thenReturn("DEVELOPER");

        // Call the POST method on the servlet
        memberServlet.doPost(requestMock, responseMock);

        // Verify that the memberService's addMember method was called once
        verify(memberServiceMock, times(1)).addMember(any(Member.class));

        // Verify the response redirection
        verify(responseMock, times(1)).sendRedirect("member?action=list");
    }

    @Test
    void testListMembers() throws ServletException, IOException {
        // Mock request parameters for listing members
        when(requestMock.getParameter("action")).thenReturn("list");
        when(memberServiceMock.getTotalMembers()).thenReturn(10);
        when(memberServiceMock.getAllMembers(anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(requestDispatcherMock);

        // Call the GET method on the servlet
        memberServlet.doGet(requestMock, responseMock);

        // Verify that the list of members was fetched
        verify(memberServiceMock, times(1)).getTotalMembers();
        verify(memberServiceMock, times(1)).getAllMembers(anyInt(), anyInt());

        // Verify forwarding to the member list JSP page
        verify(requestMock).getRequestDispatcher("/members/memberList.jsp");
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testViewMember() throws ServletException, IOException {
        // Mock request parameters for viewing a member
        when(requestMock.getParameter("action")).thenReturn("view");
        when(requestMock.getParameter("id")).thenReturn("1");
        when(memberServiceMock.getMemberById(1)).thenReturn(Optional.of(new Member(1, "John", "Doe", "john@example.com", MemberRole.DEVELOPER)));
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(requestDispatcherMock);

        // Call the GET method on the servlet
        memberServlet.doGet(requestMock, responseMock);

        // Verify that the member with ID 1 was fetched
        verify(memberServiceMock, times(1)).getMemberById(1);

        // Verify forwarding to the member view JSP page
        verify(requestMock).getRequestDispatcher("/members/memberView.jsp");
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testSearchMembers() throws ServletException, IOException {
        // Mock request parameters for searching members by email
        when(requestMock.getParameter("action")).thenReturn("search");
        when(requestMock.getParameter("email")).thenReturn("john@example.com");

        // Use Arrays.asList instead of List.of
        when(memberServiceMock.searchMembersByEmail("john@example.com"))
                .thenReturn(Arrays.asList(new Member(1, "John", "Doe", "john@example.com", MemberRole.DEVELOPER)));

        when(requestMock.getRequestDispatcher(anyString())).thenReturn(requestDispatcherMock);

        // Call the GET method on the servlet
        memberServlet.doGet(requestMock, responseMock);

        // Verify that the search by email was performed
        verify(memberServiceMock, times(1)).searchMembersByEmail("john@example.com");

        // Verify forwarding to the member list JSP page
        verify(requestMock).getRequestDispatcher("/members/memberList.jsp");
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }


    @Test
    void testUpdateMember() throws IOException, ServletException {
        // Mock request parameters for updating a member
        when(requestMock.getParameter("action")).thenReturn("update");
        when(requestMock.getParameter("id")).thenReturn("1");
        when(requestMock.getParameter("first_name")).thenReturn("John");
        when(requestMock.getParameter("last_name")).thenReturn("Doe");
        when(requestMock.getParameter("email")).thenReturn("john.doe@example.com");
        when(requestMock.getParameter("role")).thenReturn("DEVELOPER");

        // Call the POST method on the servlet
        memberServlet.doPost(requestMock, responseMock);

        // Verify that the memberService's updateMember method was called once
        verify(memberServiceMock, times(1)).updateMember(any(Member.class));

        // Verify the response redirection
        verify(responseMock, times(1)).sendRedirect("member?action=list");
    }

    @Test
    void testDeleteMember() throws IOException, ServletException {
        // Mock request parameters for deleting a member
        when(requestMock.getParameter("action")).thenReturn("delete");
        when(requestMock.getParameter("id")).thenReturn("1");

        // Call the POST method on the servlet
        memberServlet.doPost(requestMock, responseMock);

        // Verify that the memberService's deleteMember method was called once
        verify(memberServiceMock, times(1)).deleteMember(1);

        // Verify the response redirection
        verify(responseMock, times(1)).sendRedirect("member?action=list");
    }
}
