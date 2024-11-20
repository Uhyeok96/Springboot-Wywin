package com.wywin.service;


import com.wywin.dto.AuctionImgDTO;
import com.wywin.dto.AuctionItemDTO;
import com.wywin.dto.MemberDTO;
import com.wywin.entity.AuctionImg;
import com.wywin.entity.AuctionItem;
import com.wywin.entity.Bidding;
import com.wywin.entity.Member;
import com.wywin.repository.AuctionItemRepository;
import com.wywin.repository.BiddingRepository;
import com.wywin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class AuctionService {

    @Autowired
    private AuctionItemRepository auctionItemRepository; // 리포지토리 의존성 주입

    @Autowired
    private final MemberRepository memberRepository;  // MemberRepository 의존성 주입

    @Autowired
    private final BiddingRepository biddingRepository;  // BiddingRepository 의존성 주입

    private final ExchangeRateService exchangeRateService; // 환율 서비스 의존성 주입

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

        // 가격 설정: bidPrice와 동일하게 finalPrice 설정
        if (auctionItem.getFinalPrice() == null || auctionItem.getFinalPrice() == 0) {
            auctionItem.setFinalPrice(auctionItemDTO.getBidPrice()); // bidPrice와 동일하게 설정
        }

        // finalPrice를 환율 계산하여 estimatedPrice 설정
        Integer finalPrice = auctionItem.getFinalPrice();  // bidPrice와 동일
        Integer estimatedPrice = exchangeRateService.calculateEstimatedPrice(auctionItem); // 환율 계산

        // 계산된 예상견적가 설정
        auctionItem.setFinalPrice(finalPrice); // 최종 가격 (bidPrice와 동일)
        auctionItem.setEstimatedPrice(estimatedPrice); // 예상 가격

        // 예상 견적가가 계산되면, 10%로 Commission과 Penalty 설정
        if (estimatedPrice != null && estimatedPrice > 0) {
            auctionItem.setCommission(calculateCommissionOrPenalty(estimatedPrice));
            auctionItem.setPenalty(calculateCommissionOrPenalty(estimatedPrice));
        } else {
            // 예상 가격이 없다면, commission을 0으로 설정
            auctionItem.setCommission(0); // 기본값 설정
            auctionItem.setPenalty(0); // 기본값 설정
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
    public boolean validateOwner(Long auctionItemId, String loggedInUser) {
        // 아이템을 조회하고 예외 처리
        AuctionItem auctionItem = auctionItemRepository.findById(auctionItemId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        // 등록자와 로그인 사용자가 동일한지 확인
        return auctionItem.getCreatedBy().equals(loggedInUser);
    }

    // 입찰 처리
    public String placeBid(Long itemId, Integer bidAmount, String loggedInUser) {
        // 경매 아이템 조회
        AuctionItem auctionItem = auctionItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("경매 아이템을 찾을 수 없습니다."));

        // 상품 등록자와 로그인 사용자가 동일한지 확인
        if (auctionItem.getCreatedBy().equals(loggedInUser)) {
            throw new IllegalArgumentException("상품 등록자는 입찰에 참여할 수 없습니다.");  // 상품 등록자는 입찰 불가
        }

        // 현재 입찰 정보를 가져오기 (최신 입찰 기록을 조회)
        Bidding currentBid = biddingRepository.findTopByAuctionItemOrderByIdDesc(auctionItem);

        // 만약 입찰 기록이 없다면 첫 입찰
        if (currentBid == null) {
            // 첫 입찰 시 처리: 입찰 금액이 시작 가격보다 커야 함
            if (bidAmount <= auctionItem.getBidPrice()) {
                throw new IllegalArgumentException("첫 입찰 금액은 시작 가격보다 커야 합니다.");
            }
            // 첫 입찰 시 처리: finalPrice 갱신
            auctionItem.setFinalPrice(bidAmount); // finalPrice를 첫 입찰 금액으로 설정
            auctionItemRepository.save(auctionItem); // auctionItem DB에 저장

            saveNewBid(auctionItem, loggedInUser, bidAmount, null);  // 첫 입찰이므로 previousBidder는 null
            return getMemberByEmail(loggedInUser).getNickName();  // 첫 입찰자의 닉네임 반환
        }

        // 현재 입찰자와 로그인한 사용자가 동일한지 확인
        if (currentBid.getCurrentBidder().equals(loggedInUser)) {
            throw new IllegalArgumentException("동시에 여러 번 입찰은 불가합니다.");
        }

        // 입찰 금액이 최종 가격보다 높으면 finalPrice 갱신 (최초 입찰 후)
        if (bidAmount > auctionItem.getFinalPrice()) {
            auctionItem.setFinalPrice(bidAmount);  // 최종 낙찰가 갱신
            auctionItemRepository.save(auctionItem);  // 변경 사항 저장

            // 예상 견적가 계산 (finalPrice에 맞춰 환율 적용)
            Integer estimatedPrice = exchangeRateService.calculateEstimatedPrice(auctionItem);
            auctionItem.setEstimatedPrice(estimatedPrice);  // 예상 견적가 설정

            auctionItemRepository.save(auctionItem);  // 예상 견적가도 DB에 저장
        } else {
            throw new IllegalArgumentException("입찰 금액은 현재 가격보다 커야 합니다.");
        }

        // 현재 입찰자 닉네임 조회
        MemberDTO currentMember = getMemberByEmail(loggedInUser);  // 현재 입찰자 닉네임 조회
        String currentNickname = currentMember.getNickName();  // 현재 입찰자의 닉네임 가져오기

        // 이전 입찰자 닉네임을 현재 입찰자의 닉네임으로 설정하고, 기존 입찰자 정보 갱신
        String previousNickname = currentBid.getCurrentBidder(); // 기존 입찰자가 현재 입찰자였으므로 그 닉네임을 이전 입찰자에 설정

        // 새로운 입찰 기록을 생성
        Bidding newBid = new Bidding();
        newBid.setAuctionItem(auctionItem);
        newBid.setCurrentBidder(currentNickname);  // 새로운 입찰자의 닉네임을 현재 입찰자 필드에 설정
        newBid.setPreviousBidder(currentBid.getCurrentBidder());  // 기존 입찰자 닉네임을 이전 입찰자 필드에 설정
        newBid.setBiddingPrice(bidAmount);  // 새로운 입찰 금액
        newBid.setDeposit(bidAmount * 10 / 100);  // 예시로 보증금을 금액의 10%로 설정

        // 새로운 입찰 기록 저장
        biddingRepository.save(newBid);

        // 닉네임을 반환 (컨트롤러에서 사용할 수 있도록)
        return currentNickname;
    }


    // 입찰 금액이 갱신된 후 상품 정보 가져오기
    public AuctionItemDTO getUpdatedAuctionItem(Long id) {
        AuctionItem auctionItem = auctionItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        return modelMapper.map(auctionItem, AuctionItemDTO.class);
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

    // 예상 견적가의 10%로 Commission 또는 Penalty 계산
    private Integer calculateCommissionOrPenalty(Integer estimatedPrice) {
        if (estimatedPrice == null || estimatedPrice == 0) {
            return 0;  // 예상 견적가가 없으면 0 반환
        }
        // 예상 견적가의 10% 계산 (소수점 이하는 올림 처리)
        BigDecimal commissionOrPenalty = new BigDecimal(estimatedPrice).multiply(BigDecimal.valueOf(0.10));
        return commissionOrPenalty.setScale(0, RoundingMode.CEILING).intValue();  // 10%를 올림 처리한 후 Integer로 반환
    }

    // 새로운 입찰을 저장하는 메서드
    private void saveNewBid(AuctionItem auctionItem, String loggedInUser, Integer bidAmount, String previousBidder) {
        // 로그인한 사용자의 이메일을 통해 닉네임을 가져옵니다.
        MemberDTO member = getMemberByEmail(loggedInUser);
        String currentNickname = member.getNickName();  // 현재 입찰자의 닉네임

        Bidding newBid = new Bidding();
        newBid.setAuctionItem(auctionItem);
        newBid.setCurrentBidder(currentNickname);  // 첫 입찰이므로 현재 입찰자 닉네임을 설정
        newBid.setPreviousBidder(previousBidder);  // 첫 입찰인 경우 null, 이후 입찰에서는 이전 입찰자 닉네임
        newBid.setBiddingPrice(bidAmount);  // 첫 입찰 금액
        newBid.setDeposit(bidAmount * 10 / 100);  // 보증금을 10%로 설정

        biddingRepository.save(newBid);  // 새로운 입찰 기록 저장
    }


    // 최종 입찰자의 닉네임을 가져오는 메서드
    public String getFinalBidderNickName(Long auctionItemId) {
        // 경매 아이템 조회
        AuctionItem auctionItem = auctionItemRepository.findById(auctionItemId)
                .orElseThrow(() -> new RuntimeException("경매 아이템을 찾을 수 없습니다."));

        // 최신 입찰자 조회 (최신 입찰이 없으면 null)
        Bidding latestBid = biddingRepository.findTopByAuctionItemOrderByIdDesc(auctionItem);

        if (latestBid != null) {
            // 입찰자의 닉네임 (currentBidder는 이미 닉네임)
            String currentBidderNickName = latestBid.getCurrentBidder();
            log.info("최신 입찰자의 닉네임: " + currentBidderNickName); // 닉네임 확인

            // 입찰자가 있을 경우 닉네임 반환
            return currentBidderNickName;
        } else {
            log.info("최신 입찰자가 없습니다."); // 최신 입찰자가 없는 경우
        }

        // 입찰자가 없으면 기본값 "입찰자 없음" 반환
        return "입찰자 없음";
    }
}
