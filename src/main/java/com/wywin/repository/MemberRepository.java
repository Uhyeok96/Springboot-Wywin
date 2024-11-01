package com.wywin.repository;

import com.wywin.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);   // 이메일로 회원 정보 찾음
}
