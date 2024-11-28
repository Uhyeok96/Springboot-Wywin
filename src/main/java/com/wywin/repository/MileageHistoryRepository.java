package com.wywin.repository;

import com.wywin.entity.Member;
import com.wywin.entity.MileageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MileageHistoryRepository extends JpaRepository<MileageHistory, Long> {
    // 특정 회원의 마일리지 내역을 가져오는 메서드
    List<MileageHistory> findByMember(Member member);
}