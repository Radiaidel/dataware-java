package com.dataware.controller; // Replace with your actual package name

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger; // Import SLF4J Logger
import org.slf4j.LoggerFactory; // Import LoggerFactory

import com.dataware.model.Member;
import com.dataware.model.enums.MemberRole;
import com.dataware.repository.impl.MemberRepositoryImpl;
import com.dataware.service.MemberService;
import com.dataware.service.impl.MemberServiceImpl;

public class MemberServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(MemberServlet.class); // Create logger instance
    private MemberService memberService;

    public MemberServlet() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
        // Injecting the MemberRepositoryImpl into MemberServiceImpl
        memberService = new MemberServiceImpl(new MemberRepositoryImpl());
        logger.info("MemberService initialized."); // Log message on initialization
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        logger.info("Action requested: {}", action); // Log the requested action

        switch (action) {
            case "list":
                listMembers(request, response);
                break;
            case "view":
                viewMember(request, response);
                break;
            case "search":
                searchMembers(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listMembers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Pagination values
        int page = 1;          // Default page
        int pageSize = 3;      // Number of items per page

        // Get the current page number from the request
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        // Get total number of members to calculate the last page
        int totalMembers = memberService.getTotalMembers();
        int lastPage = (int) Math.ceil((double) totalMembers / pageSize);

        // Get the list of members for the current page
        List<Member> members = memberService.getAllMembers(page, pageSize);

        // Set attributes for the pagination controls
        request.setAttribute("members", members);
        request.setAttribute("currentPage", page);
        request.setAttribute("previousPage", page > 1 ? page - 1 : 1);
        request.setAttribute("nextPage", page < lastPage ? page + 1 : lastPage);
        request.setAttribute("lastPage", lastPage);

        // Log the number of members retrieved
        logger.info("Retrieved {} members for page {}.", members.size(), page);

        // Forward to the JSP page
        request.getRequestDispatcher("/members/memberList.jsp").forward(request, response);
    }

    private void viewMember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int memberId = Integer.parseInt(request.getParameter("id"));

        Optional<Member> memberOpt = memberService.getMemberById(memberId);
        if (memberOpt.isPresent()) {
            request.setAttribute("member", memberOpt.get());
            logger.info("Viewing member with ID: {}", memberId); // Log when viewing a member
            request.getRequestDispatcher("/members/memberView.jsp").forward(request, response);
        } else {
            logger.error("Member not found: ID {}", memberId); // Log error if member not found
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Member not found");
        }
    }

    private void searchMembers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Invalid email search attempt."); // Log warning for invalid email
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email");
            return;
        }

        List<Member> members = memberService.searchMembersByEmail(email);
        if (members.isEmpty()) {
            request.setAttribute("message", "No members found with the email: " + email);
            logger.info("No members found with the email: {}", email); // Log if no members found
        } else {
            request.setAttribute("members", members);
            logger.info("Found {} members with the email: {}", members.size(), email); // Log number of members found
        }

        request.getRequestDispatcher("/members/memberList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "create":
                createMember(request, response);
                break;
            case "update":
                updateMember(request, response);
                break;
            case "delete":
                deleteMember(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String roleStr = request.getParameter("role");

        MemberRole role = MemberRole.valueOf(roleStr.toUpperCase());
        Member newMember = new Member();
        newMember.setFirstName(firstName);
        newMember.setLastName(lastName);
        newMember.setEmail(email);
        newMember.setRole(role);

        memberService.addMember(newMember);
        logger.info("New member created: {}", newMember); // Log new member creation
        response.sendRedirect("member?action=list");
    }
    
    private void updateMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        Member member = new Member();
        member.setId(id);
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setEmail(email);
        member.setRole(MemberRole.valueOf(role));

        memberService.updateMember(member);
        logger.info("Member updated: ID {}", id); // Log member update
        response.sendRedirect("member?action=list");
    }

    private void deleteMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        memberService.deleteMember(id);
        logger.info("Member deleted: ID {}", id); // Log member deletion
        response.sendRedirect("member?action=list");
    }
}
