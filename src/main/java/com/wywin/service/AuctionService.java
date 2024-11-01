package com.wywin.service;


import com.wywin.dto.AuctionImgDTO;
import com.wywin.dto.AuctionItemDTO;
import com.wywin.entity.AuctionImg;
import com.wywin.entity.AuctionItem;
import com.wywin.repository.AuctionImgRepository;
import com.wywin.repository.AuctionItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

        // 이미지를 저장할 리스트 초기화
        List<AuctionImg> images = new ArrayList<>();

        // 이미지를 엔티티에 추가
        List<AuctionImgDTO> auctionImgs = auctionItemDTO.getAuctionImgs();
        for (int i = 0; i < auctionImgs.size(); i++) {
            AuctionImgDTO imgDto = auctionImgs.get(i);
            AuctionImg image = new AuctionImg();
            image.setImgName(imgDto.getImgName());
            image.setOriImgName(imgDto.getOriImgName());
            image.setImgUrl(imgDto.getImgUrl());
            image.setAuctionItem(auctionItem); // 연관된 경매 아이템 설정

            // 대표 이미지 설정: 첫 번째 이미지는 Y, 나머지는 N
            image.setRepimgYn(i == 0 ? "Y" : "N");

            images.add(image);
        }

        auctionItem.setAuctionImgs(images); // AuctionItem에 이미지 리스트 추가
        auctionItemRepository.save(auctionItem); // 리포지토리를 통해 저장
    }

    // 상품 리스트 처리 메서드
    public Page<AuctionItemDTO> getAuctionItems(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("regTime").descending()); // regTime 기준으로 내림차순 정렬

        Page<AuctionItem> auctionItems = auctionItemRepository.findAll(pageable); // 페이징된 경매 아이템 리스트 가져오기
        return auctionItems.map(item -> {
            // AuctionItem을 AuctionItemDTO로 변환
            AuctionItemDTO dto = modelMapper.map(item, AuctionItemDTO.class);

            // 해당 경매 아이템의 이미지 중 대표 이미지 설정
            AuctionImg repImg = item.getAuctionImgs().stream() // 이미지 리스트에서
                    .filter(img -> "Y".equals(img.getRepimgYn())) // 대표 이미지 필터링
                    .findFirst() // 첫 번째 결과만 가져오기
                    .orElse(null); // 없으면 null

            if (repImg != null) {
                // 대표 이미지를 포함하는 리스트로 설정
                dto.setAuctionImgs(Collections.singletonList(AuctionImgDTO.of(repImg))); // 대표 이미지만 리스트에 추가
            }

            return dto; // 변환된 DTO 반환
        });
    }



    // 상품 ID로 경매 아이템 조회
    public AuctionItemDTO getAuctionItemById(Long id) {
        AuctionItem auctionItem = auctionItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다.")); // 예외 처리
        return modelMapper.map(auctionItem, AuctionItemDTO.class); // DTO로 변환
    }
}
