package com.wywin.repository;

import com.wywin.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 회원 정보 찾음
    Member findByEmail(String email);

    // 이메일 중복 확인
    boolean existsByEmail(String email);
    // 닉네임 중복 확인
    boolean existsByNickName(String nickName);

}
