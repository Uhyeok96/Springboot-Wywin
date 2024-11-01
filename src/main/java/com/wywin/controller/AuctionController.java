package com.wywin.controller;

import com.wywin.dto.AuctionImgDTO;
import com.wywin.dto.AuctionItemDTO;
import com.wywin.service.AuctionImgService;
import com.wywin.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/auction") // http://localhost:80/auction
public class AuctionController {

    @Autowired
    private AuctionService auctionService; // 서비스 의존성 주입

    @Autowired
    private AuctionImgService auctionImgService;

    // 경매 물품 등록 폼을 보여주는 메서드
    @GetMapping("/item/new") // http://localhost:80/auction/item/new
    public String showAuctionItemForm(Model model) {
        model.addAttribute("auctionItem", new AuctionItemDTO()); // 새 DTO 객체 추가
        return "auction/auctionItemForm"; // Thymeleaf 템플릿 이름
    }

    // 경매 물품 등록을 처리하는 메서드
    @PostMapping("/item/save")
    public String saveAuctionItem(@ModelAttribute AuctionItemDTO auctionItemDTO,
                                  @RequestParam("imageFiles") MultipartFile[] imageFiles) {
        List<AuctionImgDTO> imageDtos = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            if (!imageFile.isEmpty()) {
                String imgUrl = auctionImgService.saveImageFile(imageFile); // 이미지 저장 서비스 호출
                if (imgUrl != null) {
                    String imgName = imageFile.getOriginalFilename(); // 원래 파일 이름

                    AuctionImgDTO imageDto = new AuctionImgDTO();
                    imageDto.setImgName(imgName); // 실제 파일 이름 (UUID로 저장된 파일명은 필요 없을 경우 생략 가능)
                    imageDto.setOriImgName(imgName); // 원래 파일 이름
                    imageDto.setImgUrl(imgUrl); // 저장된 이미지 URL 경로
                    imageDtos.add(imageDto);
                }
            }
        }
        auctionItemDTO.setAuctionImgs(imageDtos); // 이미지 리스트 설정

        auctionService.saveAuctionItem(auctionItemDTO); // 서비스 호출하여 저장
        return "redirect:/auction/items"; // 경매 아이템 리스트로 리다이렉트
    }

    // 경매 물품 리스트를 보여주는 메서드 (페이징 추가)
    @GetMapping("/items") // http://localhost:80/auction/items
    public String listAuctionItems(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuctionItemDTO> auctionItems = auctionService.getAuctionItems(pageable); // 서비스 호출하여 페이징된 리스트 가져오기
        model.addAttribute("auctionItems", auctionItems); // 모델에 추가
        return "auction/auctionItemList";
    }

    // 경매 물품 상세 조회 메서드
    @GetMapping("/item/{id}") // http://localhost:80/auction/item/{id}
    public String getAuctionItemDetail(@PathVariable Long id, Model model) {
        AuctionItemDTO auctionItem = auctionService.getAuctionItemById(id); // 서비스 호출
        model.addAttribute("auctionItem", auctionItem); // 모델에 추가
        return "auction/auctionItemDetail"; // 상세 페이지 템플릿 이름 변경
    }

}
