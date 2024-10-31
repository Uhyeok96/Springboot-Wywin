package com.wywin.service;


import com.wywin.dto.AuctionItemDTO;
import com.wywin.entity.AuctionImg;
import com.wywin.entity.AuctionItem;
import com.wywin.repository.AuctionImgRepository;
import com.wywin.repository.AuctionItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuctionService {

    @Autowired
    private AuctionItemRepository auctionItemRepository; // 리포지토리 의존성 주입

    private final ModelMapper modelMapper = new ModelMapper(); // DTO와 엔티티 간 변환을 위한 ModelMapper

    // 경매 아이템 등록
    public void saveAuctionItem(AuctionItemDTO auctionItemDTO) {
        AuctionItem auctionItem = modelMapper.map(auctionItemDTO, AuctionItem.class); // DTO를 엔티티로 변환

        // 이미지를 엔티티에 추가
        List<AuctionImg> images = auctionItemDTO.getAuctionImgs().stream()
                .map(imgDto -> {
                    AuctionImg image = new AuctionImg();
                    image.setImgName(imgDto.getImgName());
                    image.setOriImgName(imgDto.getOriImgName());
                    image.setImgUrl(imgDto.getImgUrl());
                    image.setAuctionItem(auctionItem); // 연관된 경매 아이템 설정
                    return image;
                }).collect(Collectors.toList());

        auctionItem.setAuctionImgs(images); // AuctionItem에 이미지 리스트 추가
        auctionItemRepository.save(auctionItem); // 리포지토리를 통해 저장
    }

    // 페이징 처리 메서드
    public Page<AuctionItemDTO> getAuctionItems(Pageable pageable) {
        return auctionItemRepository.findAll(pageable)
                .map(item -> modelMapper.map(item, AuctionItemDTO.class));
    }
}
