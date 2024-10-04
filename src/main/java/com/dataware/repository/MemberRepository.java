package com.dataware.repository;

import java.util.List;
import java.util.Optional;

import com.dataware.model.Member;

public interface MemberRepository {

	   // Create a new member
    void addMember(Member member);

    // Read a member by ID
    Optional<Member> getMemberById(int id);

    // Read all members with pagination
    List<Member> getAllMembers(int page, int pageSize);

    // Update an existing member
    void updateMember(Member member);

    // Delete a member by ID
    void deleteMember(int id);

    // Get total number of members
    int getTotalMembers();
    
    public List<Member> findByEmail(String email);
    
    List<Member> getAllMembers();

}
