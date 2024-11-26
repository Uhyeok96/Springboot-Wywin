package com.wywin.controller;

import com.wywin.dto.AuctionImgDTO;
import com.wywin.dto.AuctionItemDTO;
import com.wywin.dto.MemberDTO;
import com.wywin.entity.AuctionItem;
import com.wywin.entity.Member;
import com.wywin.service.AuctionImgService;
import com.wywin.service.AuctionService;
import com.wywin.service.ExchangeRateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/auction") // http://localhost:80/auction
public class AuctionController {

    @Autowired
    private AuctionService auctionService; // 경매 서비스 의존성 주입

    @Autowired
    private ExchangeRateService exchangeRateService; // 경매 서비스 의존성 주입

    @Autowired
    private AuctionImgService auctionImgService;    // 이미지 서비스 의존성 주입

    // 경매 물품 등록 폼을 보여주는 메서드
    @GetMapping("/item/new") // http://localhost:80/auction/item/new
    public String showAuctionItemForm(Model model) {
        model.addAttribute("auctionItem", new AuctionItemDTO()); // 새 DTO 객체 추가
        return "auction/auctionItemForm"; // Thymeleaf 템플릿 이름
    }

    // 경매 물품 등록 처리
    @PostMapping("/item/save")
    public String saveAuctionItem(@Valid @ModelAttribute AuctionItemDTO auctionItemDTO,
                                  BindingResult bindingResult,
                                  @RequestParam("imageFiles") MultipartFile[] imageFiles,
                                  Model model) {

        // 유효성 검사 오류가 있으면 폼으로 다시 돌아갑니다.
        if (bindingResult.hasErrors()) {
            model.addAttribute("auctionItem", auctionItemDTO); // 오류 발생 시, 입력된 값들을 다시 폼으로 전달
            return "auction/auctionItemForm";
        }

        // 이미지 처리
        List<AuctionImgDTO> imageDtos = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            if (!imageFile.isEmpty()) {
                String imgUrl = auctionImgService.saveImageFile(imageFile);
                if (imgUrl != null) {
                    AuctionImgDTO imageDto = new AuctionImgDTO();

                    // UUID가 적용된 이미지 이름을 저장
                    String uuidImgName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1); // /images/auction/uuid.jpg 에서 uuid.jpg 추출
                    imageDto.setImgName(uuidImgName);  // UUID 적용된 이미지 파일명
                    imageDto.setOriImgName(imageFile.getOriginalFilename());  // 원본 파일명 저장
                    imageDto.setImgUrl(imgUrl);
                    imageDtos.add(imageDto);
                }
            }
        }
        auctionItemDTO.setAuctionImgs(imageDtos);

        try {
            // 경매 아이템 저장
            auctionService.saveAuctionItem(auctionItemDTO);
        } catch (IllegalArgumentException e) {
            // 오류 발생 시 에러 메시지를 모델에 추가
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("auctionItem", auctionItemDTO); // 오류 발생 시, 입력된 값들을 다시 폼으로 전달
            return "auction/auctionItemForm";  // 오류가 있을 경우 폼으로 다시 돌아갑니다.
        }

        return "redirect:/auction/items"; // 경매 아이템 리스트로 리다이렉트
    }

    // 경매물품 리스트 출력
    @GetMapping("/items")   // http://localhost:80/auction/items
    public String listAuctionItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "gallery") String view,
            @RequestParam(defaultValue = "new") String sort,
            @RequestParam(required = false) String currencyTypes, // 수정: List<String> -> String
            Model model) {

        if (page < 1) {
            page = 1;
        }

        // currencyTypes가 null이면 빈 리스트로 초기화
        List<String> currencyTypeList = new ArrayList<>();
        if (currencyTypes != null && !currencyTypes.isEmpty()) {
            // 쉼표로 구분된 문자열을 리스트로 변환
            currencyTypeList = Arrays.asList(currencyTypes.split(","));
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        // 통화 필터링이 적용된 경우
        Page<AuctionItemDTO> auctionItems = auctionService.getAuctionItems(pageable, sort, currencyTypeList);

        model.addAttribute("auctionItems", auctionItems);
        model.addAttribute("view", view);
        model.addAttribute("sort", sort);
        model.addAttribute("currencyTypes", currencyTypeList);  // 리스트로 변환된 값 전달

        return "auction/auctionItemList"; // 일반 요청일 경우 전체 페이지 반환
    }


    // 경매 물품 상세 조회 메서드
    @GetMapping("/item/{id}") // http://localhost:80/auction/item/{id}
    public String getAuctionItemDetail(@PathVariable Long id, Model model) {
        // 로그인한 사용자의 이메일을 SecurityContextHolder에서 가져오기
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();

        // 경매 아이템 조회
        AuctionItemDTO auctionItem = auctionService.getAuctionItemById(id);
        model.addAttribute("auctionItem", auctionItem);

        // 경매 아이템의 최종 입찰자 닉네임 조회
        String finalBidderNickName = auctionService.getFinalBidderNickName(id);
        model.addAttribute("finalBidder", finalBidderNickName);  // 최종 입찰자 닉네임 모델에 추가
        System.out.println("finalBidder : " + finalBidderNickName);

        // 권한 확인: 등록자와 로그인 사용자가 동일한지 확인
        boolean canEdit = auctionService.validateOwner(id, loggedInUser);
        boolean canDelete = canEdit;  // 수정 권한이 있으면 삭제 권한도 있는 것으로 설정

        // 권한 설정
        model.addAttribute("canEdit", canEdit);
        model.addAttribute("canDelete", canDelete);

        return "auction/auctionItemDetail"; // 상세 페이지 템플릿 이름
    }

    // 경매 아이템 수정 페이지
    @GetMapping("/item/{id}/update")    // http://localhost:80/auction/item/{id}/update
    public String updateAuctionItemForm(@PathVariable("id") Long id, Model model) {
        AuctionItemDTO auctionItemDTO = auctionService.getAuctionItemById(id); // 아이템 정보 가져오기
        model.addAttribute("auctionItem", auctionItemDTO); // 수정 폼에 아이템 정보 설정
        return "auction/auctionItemEdit"; // 수정 폼 페이지 리턴
    }

    // 상품 수정 처리 메서드
    @PostMapping("/item/{id}/update")   // http://localhost:80/auction/item/{id}/update
    public String updateAuctionItem(@PathVariable("id") Long id,
                                    @Valid @ModelAttribute AuctionItemDTO auctionItemDTO,
                                    BindingResult bindingResult,
                                    @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles) {

        // 유효성 검사 오류가 있으면 폼으로 다시 돌아갑니다.
        if (bindingResult.hasErrors()) {
            return "auction/auctionEdit"; // 수정 폼으로 다시 이동
        }

        // 이미지 처리
        List<AuctionImgDTO> imageDtos = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            if (!imageFile.isEmpty()) {
                String imgUrl = auctionImgService.saveImageFile(imageFile);  // 이미지를 저장하고 URL을 얻음
                if (imgUrl != null) {
                    AuctionImgDTO imageDto = new AuctionImgDTO();
                    String uuidImgName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1); // /images/auction/uuid.jpg 에서 uuid.jpg 추출
                    imageDto.setImgName(uuidImgName);  // UUID로 된 파일명
                    imageDto.setOriImgName(imageFile.getOriginalFilename()); // 원본 파일명 저장
                    imageDto.setImgUrl(imgUrl);  // URL 경로 저장
                    imageDtos.add(imageDto);
                }
            }
        }
        auctionItemDTO.setAuctionImgs(imageDtos);

        // 경매 아이템 업데이트
        auctionService.updateAuctionItem(id, auctionItemDTO);

        return "redirect:/auction/items"; // 경매 아이템 리스트로 리다이렉트
    }

    // 경매 아이템 삭제 처리
    @PostMapping("/item/{id}/delete")   // http://localhost:80/auction/item/{id}/delete
    public String deleteAuctionItem(@PathVariable Long id, Authentication authentication) {
        // 현재 로그인한 사용자 정보 가져오기
        String loggedInUser = authentication.getName(); // 현재 로그인한 사용자 정보

        // 권한 확인 (등록자와 로그인 사용자가 동일한지 확인)
        auctionService.validateOwner(id, loggedInUser); // 권한 체크

        // 삭제 처리
        auctionService.deleteAuctionItem(id);
        return "redirect:/auction/items"; // 삭제 후 리스트 페이지로 리디렉트
    }

    // 입찰 처리 메서드
    @PostMapping("/item/{id}/bid")  // http://localhost:80/auction/item/{id}/bid
    public String placeBid(@PathVariable Long id, // 경매상품 id
                           @RequestParam Integer bidAmount, // 입찰금액
                           @RequestParam boolean agreement, // 입찰 동의 여부
                           Authentication authentication, // 로그인 사용자 정보
                           Model model) {
        // 경매 아이템을 조회 (DTO 반환)
        AuctionItemDTO auctionItemDTO = auctionService.getAuctionItemById(id);

        // 현재 로그인한 사용자
        String loggedInUser = authentication.getName(); // 로그인한 사용자의 이메일

        try {
            // 입찰 금액 유효성 검사
            if (bidAmount <= auctionItemDTO.getFinalPrice()) {
                model.addAttribute("error", "입찰 금액은 현재 가격보다 커야 합니다.");
                model.addAttribute("auctionItem", auctionItemDTO); // 상세 페이지에서 현재 경매 아이템 정보 추가
                return "auction/auctionItemDetail";  // 오류 메시지와 함께 상세 페이지로 돌아갑니다.
            }

            // 입찰 동의 여부 검사
            if (!agreement) {
                model.addAttribute("error", "입찰 동의가 필요합니다.");
                model.addAttribute("auctionItem", auctionItemDTO);
                return "auction/auctionItemDetail";  // 동의하지 않으면 오류 메시지와 함께 상세 페이지로 돌아갑니다.
            }

            // 입찰 처리 (상품 등록자와 로그인 사용자가 동일한지 체크)
            String currentNickname = auctionService.placeBid(id, bidAmount, loggedInUser); // 서비스에서 닉네임을 반환받음
            System.out.println("Current Bidder: " + currentNickname);  // 디버깅을 위한 로그

            // 입찰 후 최신 finalPrice 값을 다시 반환
            auctionItemDTO = auctionService.getUpdatedAuctionItem(id); // 최신 정보를 다시 가져옵니다.

            // 모델에 최종 입찰 정보와 예상 견적가를 추가
            model.addAttribute("auctionItem", auctionItemDTO);
            model.addAttribute("finalBidder", currentNickname);  // 닉네임을 모델에 추가
            model.addAttribute("estimatedPrice", auctionItemDTO.getEstimatedPrice());  // 예상 견적가 추가

            // 상품 등록자 여부 체크 (수정, 삭제 권한 추가)
            boolean canEdit = auctionItemDTO.getCreatedBy().equals(loggedInUser);  // 상품 등록자 여부
            boolean canDelete = auctionItemDTO.getCreatedBy().equals(loggedInUser);  // 삭제 권한 (상품 등록자만 가능)
            model.addAttribute("canEdit", canEdit); // 수정 가능 여부
            model.addAttribute("canDelete", canDelete); // 삭제 가능 여부

            // 포워딩을 사용하여 뷰 반환
            return "auction/auctionItemDetail";

        } catch (IllegalArgumentException e) {
            // 상품 등록자가 입찰을 시도하면 예외가 발생
            model.addAttribute("error", e.getMessage()); // 에러 메시지 추가
            model.addAttribute("auctionItem", auctionItemDTO); // 상세 페이지 정보 다시 전달

            // 예외 발생 후에도 최종 입찰자 정보 갱신
            String finalBidderNickName = auctionService.getFinalBidderNickName(id);
            model.addAttribute("finalBidder", finalBidderNickName);  // 예외가 발생하더라도 최종 입찰자 닉네임 추가

            // 상품 등록자 여부 체크 (수정, 삭제 권한 추가) - 예외가 발생해도 수정, 삭제 버튼이 보이도록
            boolean canEdit = auctionItemDTO.getCreatedBy().equals(loggedInUser);  // 상품 등록자 여부
            boolean canDelete = auctionItemDTO.getCreatedBy().equals(loggedInUser);  // 삭제 권한 (상품 등록자만 가능)

            model.addAttribute("canEdit", canEdit); // 수정 가능 여부
            model.addAttribute("canDelete", canDelete); // 삭제 가능 여부

            // 경고 메시지와 함께 상세 페이지로 돌아감
            return "auction/auctionItemDetail";
        }
    }

    // 경매 이력 페이지
    @GetMapping("/history") // http://localhost:80/auction/history
    public String getAuctionHistory(Authentication authentication, Model model) {
        // 로그인한 사용자의 이메일 가져오기
        String loggedInUserEmail = authentication.getName();

        // 경매 이력과 닉네임 가져오기
        Map<String, Object> auctionHistory = auctionService.getAuctionHistory(loggedInUserEmail);

        // 모델에 데이터 추가
        model.addAttribute("nickname", auctionHistory.get("nickname")); // 닉네임
        model.addAttribute("biddingDTOs", auctionHistory.get("biddingDTOs")); // 입찰 정보
        model.addAttribute("auctionItemDTOs", auctionHistory.get("auctionItemDTOs")); // 경매 아이템 정보

        return "auction/auctionItemHistory";  // Thymeleaf 템플릿 반환
    }

}