package com.wywin.service;

import com.wywin.entity.Member;
import com.wywin.entity.MileageHistory;
import com.wywin.repository.MileageHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MileageHistoryService {
    @Autowired
    private MileageHistoryRepository mileageHistoryRepository;

    // 마일리지 내역 기록
    public void logHistory(Member member, int amount, String type, String description) {
        MileageHistory history = new MileageHistory();
        history.setMember(member);
        history.setAmount(amount);
        history.setType(type);
        history.setDescription(description);
        history.setTransactionDate(LocalDateTime.now());
        mileageHistoryRepository.save(history);
    }

    // 회원의 마일리지 내역 조회
    public List<MileageHistory> getHistoryForMember(Member member) {
        return mileageHistoryRepository.findByMember(member);
    }
}
