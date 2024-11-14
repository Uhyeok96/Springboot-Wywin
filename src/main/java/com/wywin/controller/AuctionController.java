package com.wywin.controller;

import com.wywin.dto.AuctionImgDTO;
import com.wywin.dto.AuctionItemDTO;
import com.wywin.dto.MemberDTO;
import com.wywin.service.AuctionImgService;
import com.wywin.service.AuctionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/auction") // http://localhost:80/auction
public class AuctionController {

    @Autowired
    private AuctionService auctionService; // 경매 서비스 의존성 주입

    @Autowired
    private AuctionImgService auctionImgService;    // 이미지 서비스 의존성 주입

    // 경매 물품 등록 폼을 보여주는 메서드
    @GetMapping("/item/new") // http://localhost:80/auction/item/new
    public String showAuctionItemForm(Model model) {
        model.addAttribute("auctionItem", new AuctionItemDTO()); // 새 DTO 객체 추가
        return "auction/auctionItemForm"; // Thymeleaf 템플릿 이름
    }

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


    // 경매 물품 리스트를 보여주는 메서드 (페이징 추가)
    @GetMapping("/items")
    public String listAuctionItems(Model model,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "gallery") String view) {
        // 페이지 번호가 1보다 작으면 1로 설정
        if (page < 1) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size); // 0 기반 인덱스를 위해 1을 뺍니다.
        Page<AuctionItemDTO> auctionItems = auctionService.getAuctionItems(pageable);
        model.addAttribute("auctionItems", auctionItems);
        model.addAttribute("view", view);
        return "auction/auctionItemList";
    }

    // 경매 물품 상세 조회 메서드
    @GetMapping("/item/{id}") // http://localhost:80/auction/item/{id}
    public String getAuctionItemDetail(@PathVariable Long id, Model model) {
        // 로그인한 사용자의 이메일을 SecurityContextHolder에서 가져오기
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();

        // 경매 아이템 조회
        AuctionItemDTO auctionItem = auctionService.getAuctionItemById(id);
        model.addAttribute("auctionItem", auctionItem);

        // 권한 확인: 등록자와 로그인 사용자가 동일한지 확인
        auctionService.validateOwner(id, loggedInUser); // 권한 확인 (수정/삭제 권한이 있을 경우)
        model.addAttribute("canEdit", true); // 수정 권한 있음
        model.addAttribute("canDelete", true); // 삭제 권한 있음

        return "auction/auctionItemDetail"; // 상세 페이지 템플릿 이름
    }

    // 경매 아이템 수정 페이지
    @GetMapping("/item/{id}/update")
    public String updateAuctionItemForm(@PathVariable("id") Long id, Model model) {
        AuctionItemDTO auctionItemDTO = auctionService.getAuctionItemById(id); // 아이템 정보 가져오기
        model.addAttribute("auctionItem", auctionItemDTO); // 수정 폼에 아이템 정보 설정
        return "auction/auctionItemEdit"; // 수정 폼 페이지 리턴
    }

    // 상품 수정 처리 메서드
    @PostMapping("/item/{id}/update")
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
    @PostMapping("/item/{id}/delete")
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
    @PostMapping("/item/{id}/bid")
    public String placeBid(@PathVariable Long id, // 경매상품 id
                           @RequestParam Integer bidAmount, // 입찰금액
                           @RequestParam boolean agreement, // 입찰 동의를 받을 수 있도록 추가
                           Authentication authentication, // 현재 로그인한 사용자 정보
                           Model model) {
        // 경매 아이템을 조회
        AuctionItemDTO auctionItem = auctionService.getAuctionItemById(id);

        // 현재 로그인한 사용자
        String loggedInUser = authentication.getName(); // 이메일 (로그인된 사용자)

        // 이메일을 이용해 회원 정보 조회
        MemberDTO member = auctionService.getMemberByEmail(loggedInUser);  // AuctionService에서 이메일로 회원 조회

        // 회원 정보에서 닉네임 가져오기
        String nickname = member.getNickName(); // 최종 입찰자 닉네임

        // 입찰 금액 유효성 검사
        if (bidAmount <= auctionItem.getFinalPrice()) {
            model.addAttribute("error", "입찰 금액은 현재 가격보다 커야 합니다.");
            model.addAttribute("auctionItem", auctionItem); // 상세 페이지에서 현재 경매 아이템 정보 추가
            return "auction/auctionItemDetail";  // 오류 메시지와 함께 상세 페이지로 돌아갑니다.
        }

        // 입찰 동의 여부 검사
        if (!agreement) {
            model.addAttribute("error", "입찰 동의가 필요합니다.");
            model.addAttribute("auctionItem", auctionItem);
            return "auction/auctionItemDetail";  // 동의하지 않으면 오류 메시지와 함께 상세 페이지로 돌아갑니다.
        }

        // 입찰 처리
        auctionService.placeBid(id, bidAmount);

        // 입찰 후 최신 finalPrice 값을 다시 반환
        auctionItem = auctionService.getAuctionItemById(id); // 최신 정보를 다시 가져옵니다.
        model.addAttribute("auctionItem", auctionItem);

        // 최종 입찰자 정보 추가
        model.addAttribute("finalBidder", nickname);  // 닉네임을 모델에 추가

        // 성공적으로 입찰한 후 경매 아이템 상세 페이지로 리디렉션
        return "redirect:/auction/item/" + id;
    }

}