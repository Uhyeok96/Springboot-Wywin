package com.wywin.service;

import com.wywin.dto.MemberDTO;
import com.wywin.entity.Member;
import com.wywin.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberDTO getMember(String email) {
        Member member = memberRepository.findByEmail(email);
        return member != null ? member.toDTO() : null;
    }
}
