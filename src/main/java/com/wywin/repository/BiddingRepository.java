package com.wywin.repository;

import com.wywin.entity.AuctionItem;
import com.wywin.entity.Bidding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Bidding 엔티티에 대한 JPA Repository 인터페이스
public interface BiddingRepository extends JpaRepository<Bidding, Long> {

    // 최신 입찰을 가져오는 메서드 (최신 입찰자 순으로 정렬)
    Bidding findTopByAuctionItemOrderByIdDesc(AuctionItem auctionItem);

    // 특정 경매 아이템에 대한 모든 입찰 기록을 가져오는 메서드 (입찰 금액 기준 정렬)
    List<Bidding> findByAuctionItemOrderByBiddingPriceDesc(AuctionItem auctionItem);
}
