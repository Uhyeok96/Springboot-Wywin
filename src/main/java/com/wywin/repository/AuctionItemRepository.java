package com.wywin.repository;

import com.wywin.entity.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {

    // 상품명 중복 확인
    boolean existsByItemName(String itemName);
}
