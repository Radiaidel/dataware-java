package com.dataware.service;


import java.util.List;
import java.util.Optional;

import com.dataware.model.Member;

public interface MemberService {
    // Add a new member
    void addMember(Member member);

    // Get a member by ID
    Optional<Member> getMemberById(int id);

    // Get all members with pagination
    List<Member> getAllMembers(int page, int pageSize);

    // Update an existing member
    void updateMember(Member member);

    // Delete a member by ID
    void deleteMember(int id);

    // Get total number of members
    int getTotalMembers();
    
    List<Member> searchMembersByEmail(String email);
    
    List<Member> getAllMembers();


}