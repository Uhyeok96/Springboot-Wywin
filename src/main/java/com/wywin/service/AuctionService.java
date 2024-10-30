package com.wywin.service;

import com.wywin.dto.AuctionItemDTO;

import java.util.List;

public interface AuctionService {

    void saveAuctionItem(AuctionItemDTO auctionItemDTO); // 경매 물품 등록

    List<AuctionItemDTO> getAllAuctionItems(); // 모든 경매 물품 리스트 가져오기
}
