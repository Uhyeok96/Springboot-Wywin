package com.wywin.service;

import com.wywin.constant.ItemStatus;
import com.wywin.entity.AuctionItem;
import com.wywin.repository.AuctionItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionSchedulerService {

    @Autowired
    private AuctionItemRepository auctionItemRepository;    // 의존성 주입

    // 1분마다 경매 종료 시간을 체크해서 경매시간이 종료된 상품들 상태를 OFF로 변경
    @Scheduled(cron = "0 * * * * *")  // 매 분마다 실행
    @Transactional  // 트랜잭션을 적용하여 DB에 저장이 제대로 이루어지도록 보장
    public void checkAuctionEndTimes() {
        // 경매 종료 시간이 지난 아이템들 조회
        List<AuctionItem> auctionItems = auctionItemRepository
                .findByAuctionEndDateBeforeAndItemStatusNot(LocalDateTime.now(), ItemStatus.OFF);

        // 경매 종료된 아이템들 상태를 'OFF'로 변경
        for (AuctionItem auctionItem : auctionItems) {
            auctionItem.setItemStatus(ItemStatus.OFF); // enum 값 사용
            auctionItemRepository.save(auctionItem); // 상태 변경 저장
        }
    }
}
