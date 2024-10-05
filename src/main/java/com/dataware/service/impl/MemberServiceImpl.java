package com.dataware.service.impl;

import com.dataware.model.Member;
import com.dataware.repository.MemberRepository;
import com.dataware.service.MemberService;

import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    // Constructor for Dependency Injection
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addMember(Member member) {
        memberRepository.addMember(member);
    }

    @Override
    public Optional<Member> getMemberById(int id) {
        return memberRepository.getMemberById(id);
    }

    @Override
    public List<Member> getAllMembers(int page, int pageSize) {
        return memberRepository.getAllMembers(page, pageSize);
    }
    
    @Override
    public List<Member> getAllMembers() {
        return memberRepository.getAllMembers();
    }

    @Override
    public void updateMember(Member member) {
        memberRepository.updateMember(member);
    }

    @Override
    public void deleteMember(int id) {
        memberRepository.deleteMember(id);
    }

    @Override
    public int getTotalMembers() {
        return memberRepository.getTotalMembers();
    }
    
    @Override
    public List<Member> searchMembersByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
