package com.wywin.repository;

import com.wywin.constant.ItemStatus;
import com.wywin.entity.AuctionItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ItemAuctionRepositoryTest {

    @Autowired
    AuctionItemRepository itemAuctionRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){                       // 아이템 생성 테스트
        AuctionItem item = new AuctionItem();
        item.setItemName("테스트 상품");
        item.setCommission(10000);
        item.setFinalPrice(10000);
        item.setBidPrice(10000);
        item.setItemStatus(ItemStatus.SELL);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        AuctionItem savedItem = itemAuctionRepository.save(item);         // Jpa를 이용해 insert 처리
        System.out.println(savedItem.toString());           // 출력
    }

}