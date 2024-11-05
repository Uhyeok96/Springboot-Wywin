package com.wywin.controller;

import com.wywin.dto.AuctionImgDTO;
import com.wywin.dto.AuctionItemDTO;
import com.wywin.service.AuctionImgService;
import com.wywin.service.AuctionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String saveAuctionItem(@Valid @ModelAttribute AuctionItemDTO auctionItemDTO,
                                  BindingResult bindingResult,
                                  @RequestParam("imageFiles") MultipartFile[] imageFiles) {

        // 유효성 검사 오류가 있으면 폼으로 다시 돌아갑니다.
        if (bindingResult.hasErrors()) {
            return "auction/auctionItemForm";
        }

        // 이미지 처리
        List<AuctionImgDTO> imageDtos = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            if (!imageFile.isEmpty()) {
                String imgUrl = auctionImgService.saveImageFile(imageFile);
                if (imgUrl != null) {
                    AuctionImgDTO imageDto = new AuctionImgDTO();
                    imageDto.setImgName(imageFile.getOriginalFilename());
                    imageDto.setOriImgName(imageFile.getOriginalFilename());
                    imageDto.setImgUrl(imgUrl);
                    imageDtos.add(imageDto);
                }
            }
        }
        auctionItemDTO.setAuctionImgs(imageDtos);

        // 경매 아이템 저장
        auctionService.saveAuctionItem(auctionItemDTO);

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
        AuctionItemDTO auctionItem = auctionService.getAuctionItemById(id); // 서비스 호출
        model.addAttribute("auctionItem", auctionItem); // 모델에 추가
        return "auction/auctionItemDetail"; // 상세 페이지 템플릿 이름 변경
    }

}