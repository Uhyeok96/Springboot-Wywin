package com.wywin.controller;

import com.wywin.dto.AuctionItemDTO;
import com.wywin.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/auction") // http://localhost:80/auction
public class AuctionController {

    @Autowired
    private AuctionService auctionService; // 서비스 의존성 주입

    // 경매 물품 등록 폼을 보여주는 메서드
    @GetMapping("/item/new")    // http://localhost:80/auction/item/new
    public String showAuctionItemForm(Model model) {
        model.addAttribute("auctionItem", new AuctionItemDTO()); // 새 DTO 객체 추가
        return "auction/auctionItemForm"; // Thymeleaf 템플릿 이름
    }

    // 경매 물품 등록을 처리하는 메서드
    @PostMapping("/item/save")  // http://localhost:80/auction/item/save
    public String saveAuctionItem(@ModelAttribute AuctionItemDTO auctionItemDTO) {
        auctionService.saveAuctionItem(auctionItemDTO); // 서비스 호출하여 저장
        return "redirect:/auction/items"; // 경매 아이템 리스트로 리다이렉트
    }

    // 경매 물품 리스트를 보여주는 메서드
    @GetMapping("/items")   // http://localhost:80/auction/items
    public String listAuctionItems(Model model) {
        List<AuctionItemDTO> auctionItems = auctionService.getAllAuctionItems(); // 서비스 호출하여 리스트 가져오기
        model.addAttribute("auctionItems", auctionItems); // 모델에 추가
        return "auction/auctionItemList"; // 경매 아이템 리스트를 보여줄 Thymeleaf 템플릿 이름
    }
}
