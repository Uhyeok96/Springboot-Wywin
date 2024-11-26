package com.wywin.repository;

import com.wywin.entity.AuctionItem;
import com.wywin.entity.Bidding;
import com.wywin.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Bidding 엔티티에 대한 JPA Repository 인터페이스
public interface BiddingRepository extends JpaRepository<Bidding, Long> {

    // 최신 입찰을 가져오는 메서드 (최신 입찰자 순으로 정렬)
    Bidding findTopByAuctionItemOrderByIdDesc(AuctionItem auctionItem);

    // 특정 경매 아이템에 대한 모든 입찰 기록을 가져오는 메서드 (입찰 금액 기준 정렬)
    List<Bidding> findByAuctionItemOrderByBiddingPriceDesc(AuctionItem auctionItem);

    // 로그인한 사용자의 이메일과 createdBy를 비교하여 입찰 내역 조회
    @Query("SELECT b FROM Bidding b JOIN FETCH b.auctionItem WHERE b.createdBy = :loggedInUserEmail")
    List<Bidding> findBiddingsByCreatedBy(@Param("loggedInUserEmail") String loggedInUserEmail);

    // 특정 경매 아이템에 대해 로그인한 사용자가 한 마지막 입찰을 가져오는 메서드
    @Query("SELECT b FROM Bidding b WHERE b.auctionItem = :auctionItem AND b.createdBy = :loggedInUserEmail ORDER BY b.id DESC")
    List<Bidding> findLatestBiddingByAuctionItemAndUser(@Param("auctionItem") AuctionItem auctionItem, @Param("loggedInUserEmail") String loggedInUserEmail);

}
