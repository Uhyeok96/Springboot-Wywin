package com.wywin.repository;

import com.wywin.entity.AuctionImg;
import com.wywin.entity.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionImgRepository extends JpaRepository<AuctionImg, Long> {

    // 경매 아이템에 속한 이미지 삭제
    void deleteByAuctionItem(AuctionItem auctionItem);

}
