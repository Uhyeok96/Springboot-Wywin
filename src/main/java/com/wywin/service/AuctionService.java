package com.wywin.service;


import com.wywin.dto.AuctionImgDTO;
import com.wywin.dto.AuctionItemDTO;
import com.wywin.dto.MemberDTO;
import com.wywin.entity.AuctionImg;
import com.wywin.entity.AuctionItem;
import com.wywin.entity.Member;
import com.wywin.repository.AuctionItemRepository;
import com.wywin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuctionService {

    @Autowired
    private AuctionItemRepository auctionItemRepository; // 리포지토리 의존성 주입

    @Autowired
    private final MemberRepository memberRepository;  // MemberRepository 의존성 주입

    private final AuctionImgService auctionImgService; // 이미지 서비스 의존성 주입

    private final ModelMapper modelMapper = new ModelMapper(); // DTO와 엔티티 간 변환을 위한 ModelMapper


    // 경매 아이템 등록
    public void saveAuctionItem(AuctionItemDTO auctionItemDTO) {
        // 중복된 상품명 확인
        boolean isDuplicate = auctionItemRepository.existsByItemName(auctionItemDTO.getItemName());
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 등록된 상품명입니다.");
        }

        // AuctionItemDTO를 AuctionItem 엔티티로 변환
        AuctionItem auctionItem = modelMapper.map(auctionItemDTO, AuctionItem.class);

        // 각 필드에 기본값을 설정
        if (auctionItem.getDeposit() == null) {
            auctionItem.setDeposit(0); // 기본값 설정
        }
        if (auctionItem.getCommission() == null) {
            auctionItem.setCommission(0); // 기본값 설정
        }
        if (auctionItem.getPenalty() == null) {
            auctionItem.setPenalty(0); // 기본값 설정
        }
        if (auctionItem.getFinalPrice() == null) {
            auctionItem.setFinalPrice(0); // 기본값 설정
        }

        // 경매 종료일 계산
        LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(auctionItemDTO.getAuctionPeriod());
        auctionItem.setAuctionEndDate(auctionEndDate);  // 경매 종료일 설정

        // 이미지 처리
        List<AuctionImg> images = new ArrayList<>();
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

        auctionItem.setAuctionImgs(images); // 이미지 리스트 설정

        // 경매 아이템 저장
        auctionItemRepository.save(auctionItem);
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
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // AuctionItem을 AuctionItemDTO로 변환
        AuctionItemDTO dto = modelMapper.map(auctionItem, AuctionItemDTO.class);

        // Auditing 정보를 DTO에 추가
        dto.setCreatedBy(auctionItem.getCreatedBy());
        dto.setModifiedBy(auctionItem.getModifiedBy());

        return dto;
    }

    // 경매 아이템 수정
    public void updateAuctionItem(Long id, AuctionItemDTO auctionItemDTO) {
        // 기존 경매 아이템 조회
        AuctionItem auctionItem = auctionItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        // 기존 데이터 수정
        auctionItem.setItemName(auctionItemDTO.getItemName());
        auctionItem.setItemShortDetail(auctionItemDTO.getItemShortDetail());
        auctionItem.setItemLongDetail(auctionItemDTO.getItemLongDetail());
        auctionItem.setBidPrice(auctionItemDTO.getBidPrice());
        auctionItem.setAuctionPeriod(auctionItemDTO.getAuctionPeriod());

        // 경매 종료 일시 수정하지 않음
        // 기존 종료일을 그대로 유지
        LocalDateTime existingEndDate = auctionItem.getAuctionEndDate();
        auctionItem.setAuctionEndDate(existingEndDate);

        // 기존 이미지를 삭제
        List<AuctionImg> existingImages = auctionItem.getAuctionImgs();
        auctionImgService.deleteImages(existingImages); // 기존 이미지를 삭제

        // 새로 추가할 이미지 리스트
        List<AuctionImg> newImages = new ArrayList<>();
        List<AuctionImgDTO> auctionImgs = auctionItemDTO.getAuctionImgs();

        // 새 이미지를 처리
        for (int i = 0; i < auctionImgs.size(); i++) {
            AuctionImgDTO imgDto = auctionImgs.get(i);
            AuctionImg image = new AuctionImg();

            // UUID로 새로운 이미지명 생성 (이미 uuidImgName을 받아왔으므로 그대로 사용)
            String imgName = imgDto.getImgName(); // uuidImgName을 사용
            String imgUrl = "/images/auction/" + imgName; // URL 경로

            // 이미지 정보 세팅
            image.setImgName(imgName);      // UUID로 생성된 이미지 이름
            image.setOriImgName(imgDto.getOriImgName());  // 원본 이미지 파일명
            image.setImgUrl(imgUrl);        // UUID로 생성된 이미지 URL
            image.setAuctionItem(auctionItem); // 연관된 경매 아이템 설정
            image.setRepimgYn(i == 0 ? "Y" : "N"); // 첫 번째 이미지는 대표 이미지로 설정

            newImages.add(image); // 새 이미지 리스트에 추가
        }

        // 새로 추가된 이미지를 경매 아이템에 설정
        auctionItem.getAuctionImgs().clear(); // 기존 이미지를 모두 비우고
        auctionItem.getAuctionImgs().addAll(newImages); // 새 이미지들로 추가

        // 경매 아이템 업데이트
        auctionItemRepository.save(auctionItem); // 경매 아이템 저장
    }

    // 경매 아이템 삭제
    public void deleteAuctionItem(Long id) {
        AuctionItem auctionItem = auctionItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        // 해당 아이템의 이미지를 삭제
        auctionImgService.deleteImages(auctionItem.getAuctionImgs());

        auctionItemRepository.delete(auctionItem); // 아이템 삭제
    }

    // 경매 아이템 소유자 확인
    public void validateOwner(Long auctionItemId, String loggedInUser) {
        // 아이템을 조회하고 예외 처리
        AuctionItem auctionItem = auctionItemRepository.findById(auctionItemId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        // 등록자와 로그인 사용자가 동일한지 확인
        if (!auctionItem.getCreatedBy().equals(loggedInUser)) {
            throw new RuntimeException("수정 또는 삭제할 권한이 없습니다.");
        }
    }

    // 입찰 처리
    public void placeBid(Long itemId, Integer bidAmount) {
        // 경매 아이템 조회
        AuctionItem auctionItem = auctionItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("경매 아이템을 찾을 수 없습니다."));

        // 입찰 금액이 시작 가격보다 낮으면 예외 처리
        if (bidAmount < auctionItem.getBidPrice()) {
            throw new IllegalArgumentException("입찰 금액은 시작 가격보다 커야 합니다.");
        }

        // 입찰 금액이 최종 가격보다 높으면 finalPrice 갱신
        if (auctionItem.getFinalPrice() == null || bidAmount > auctionItem.getFinalPrice()) {
            auctionItem.setFinalPrice(bidAmount);  // 최종 낙찰가 갱신
            auctionItemRepository.save(auctionItem);  // 변경 사항 저장
        } else {
            throw new IllegalArgumentException("입찰 금액은 현재 가격보다 커야 합니다.");
        }
    }

    // 이메일을 이용해 회원 정보를 조회하는 메서드
    public MemberDTO getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email);  // 이메일로 회원 정보 조회

        if (member == null) {
            throw new RuntimeException("회원 정보를 찾을 수 없습니다.");
        }

        // Member 엔티티를 MemberDTO로 변환하여 반환
        return modelMapper.map(member, MemberDTO.class);
    }

}
