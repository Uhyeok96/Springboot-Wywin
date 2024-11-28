package com.wywin.controller;

import com.wywin.dto.AuctionItemDTO;
import com.wywin.dto.ItemSearchDTO;
import com.wywin.dto.SellingItemDTO;
import com.wywin.dto.SellingItemFormDTO;
import com.wywin.entity.Member;
import com.wywin.entity.SellingItem;
import com.wywin.repository.AuctionItemRepository;
import com.wywin.repository.MemberRepository;
import com.wywin.service.AuctionService;
import com.wywin.service.SellingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AuctionService auctionService;
    private final AuctionItemRepository auctionItemRepository;
    private final SellingService sellingService;
    private final MemberRepository memberRepository;

    @GetMapping(value = "/")
    public String listAuctionItems(Model model,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "gallery") String view) {
        // 페이지 번호가 1보다 작으면 1로 설정
        if (page < 1) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size); // 0 기반 인덱스를 위해 1을 뺍니다.
        Page<AuctionItemDTO> auctionItems = auctionService.getAuctionItemsList(pageable);
        model.addAttribute("auctionList", auctionItems);
        model.addAttribute("view", view);
        return "admin/adminMain";
    }

    // 경매 물품 상세 조회 메서드
    @GetMapping("/auction/{id}") // http://localhost:80/auction/item/{id}
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

        return "admin/detailAuction"; // 상세 페이지 템플릿 이름
    }

    // 경매 아이템 삭제 처리
    @PostMapping("/auction/delete/{id}")
    public String deleteAuctionItem(@PathVariable Long id) {
        // 삭제 처리
        auctionItemRepository.deleteById(id);
        return "redirect:/admin/adminMain"; // 삭제 후 리스트 페이지로 리디렉트
    }

    // 판매상품 리스트
    @GetMapping(value = {"/sellList", "/sellList/{page}"})  //페이징이 없는경우, 있는 경우
    public String itemManage(ItemSearchDTO itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.orElse(0), 20);
        // 페이지 파라미터가 없으면 0번 페이지를 보임. 한 페이지당 3개의 상품만 보여줌.
        Page<SellingItem> items = sellingService.getSellingItemPage(itemSearchDto, pageable);  // 조회 조건, 페이징 정보를 파라미터로 넘겨서 Page 타입으로 받음
        // 조회 조건과 페이징 정보를 파라미터로 넘겨서 item 객체 받음
        model.addAttribute("items", items); // 조회한 상품 데이터 및 페이징정보를 뷰로 전달
        model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환시 기존 검색 조건을 유지
        model.addAttribute("maxPage", 5);   // 상품관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수 5

        return "admin/adminSellList";
    }

    // 상세페이지 조회
    @GetMapping(value = "/sellDetail/{sellingId}")  // 경로를 명확하게 변경
    public String itemDtl(Model model, @PathVariable("sellingId") Long sellingId, Principal principal) {
        SellingItemFormDTO sellingItemFormDTO = sellingService.getItemDtl(sellingId);
        model.addAttribute("item", sellingItemFormDTO);

        SellingItem sellingItem = sellingService.getSellingItem(sellingId);
        boolean isUploader = false;
        if (principal != null && sellingItem.getSeller() != null) {
            isUploader = sellingItem.getSeller().equals(principal.getName());
        }
        model.addAttribute("isUploader", isUploader);

        return "admin/adminSellDetail";
    }

    @GetMapping(value = "/memberList")
    public String memberList(Model model){
        List<Member> memberList = memberRepository.findAll();
        model.addAttribute("member", memberList);

        return "admin/adminMemberList";
    }

}
