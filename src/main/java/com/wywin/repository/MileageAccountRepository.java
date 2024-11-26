package com.wywin.repository;

import com.wywin.entity.MileageAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MileageAccountRepository extends JpaRepository<MileageAccount, Long> {

    // 회원의 이메일로 마일리지 계좌를 조회
    Optional<MileageAccount> findByMemberEmail(String email);

    // 회원의 닉네임으로 마일리지 계좌를 조회
    Optional<MileageAccount> findByMemberNickName(String nickname);
}
