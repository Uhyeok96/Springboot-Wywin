package com.wywin.repository;

import com.wywin.constant.ItemStatus;
import com.wywin.entity.AuctionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {

    // 상품명 중복 확인
    boolean existsByItemName(String itemName);

    // 선택된 통화 목록에 포함된 상품 조회 (페이징 지원)
    Page<AuctionItem> findByCurrencyTypeIn(List<String> currencyTypes, Pageable pageable);

    // 입찰 수 기준 내림차순 정렬 (입찰 수 많을수록 먼저) + 통화 필터링
    @Query("SELECT a FROM AuctionItem a LEFT JOIN a.biddings b WHERE a.currencyType IN :currencyTypes GROUP BY a.id ORDER BY COUNT(b) DESC")
    Page<AuctionItem> findByCurrencyTypeInAndBiddingCountDesc(List<String> currencyTypes, Pageable pageable);

    // 입찰 수 기준 오름차순 정렬 (입찰 수 적을수록 먼저) + 통화 필터링
    @Query("SELECT a FROM AuctionItem a LEFT JOIN a.biddings b WHERE a.currencyType IN :currencyTypes GROUP BY a.id ORDER BY COUNT(b) ASC")
    Page<AuctionItem> findByCurrencyTypeInAndBiddingCountAsc(List<String> currencyTypes, Pageable pageable);

    // 입찰 수 기준 내림차순 정렬 (통화 필터링 없이)
    @Query("SELECT a FROM AuctionItem a LEFT JOIN a.biddings b GROUP BY a.id ORDER BY COUNT(b) DESC")
    Page<AuctionItem> findByBiddingCountDesc(Pageable pageable);

    // 입찰 수 기준 오름차순 정렬 (통화 필터링 없이)
    @Query("SELECT a FROM AuctionItem a LEFT JOIN a.biddings b GROUP BY a.id ORDER BY COUNT(b) ASC")
    Page<AuctionItem> findByBiddingCountAsc(Pageable pageable);

    // 경매 종료 시간이 현재 시간 이전이고, 'OFF' 상태가 아닌 아이템을 찾는 쿼리
    List<AuctionItem> findByAuctionEndDateBeforeAndItemStatusNot(LocalDateTime now, ItemStatus status);
}
