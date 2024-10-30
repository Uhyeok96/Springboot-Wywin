package com.wywin.service;

import com.wywin.dto.AuctionItemDTO;
import com.wywin.entity.AuctionItem;
import com.wywin.repository.AuctionItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// AuctionService 인터페이스의 구현체
@Service
public class AuctionServiceImpl implements AuctionService{

    @Autowired
    private AuctionItemRepository auctionItemRepository; // 리포지토리 의존성 주입

    private final ModelMapper modelMapper = new ModelMapper(); // DTO와 엔티티 간 변환을 위한 ModelMapper

    // 경매 아이템 등록
    @Override
    public void saveAuctionItem(AuctionItemDTO auctionItemDTO) {
        AuctionItem auctionItem = modelMapper.map(auctionItemDTO, AuctionItem.class); // DTO를 엔티티로 변환
        auctionItemRepository.save(auctionItem); // 리포지토리를 통해 저장
    }

    // 경매 아이템 리스트
    @Override
    public List<AuctionItemDTO> getAllAuctionItems() {
        return auctionItemRepository.findAll().stream() // 모든 경매 아이템 조회
                .map(item -> modelMapper.map(item, AuctionItemDTO.class)) // 엔티티를 DTO로 변환
                .collect(Collectors.toList()); // 리스트로 수집
    }
}
