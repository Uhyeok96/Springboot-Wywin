package com.wywin.controller;

import com.wywin.dto.CommentDTO;
import com.wywin.dto.ItemFormDTO;
import com.wywin.dto.ItemSearchDTO;
import com.wywin.dto.SellingItemFormDTO;
import com.wywin.entity.Comment;
import com.wywin.entity.Item;
import com.wywin.entity.Member;
import com.wywin.entity.SellingItem;
import com.wywin.repository.MemberRepository;
import com.wywin.service.CommentService;
import com.wywin.service.ItemService;
import com.wywin.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @GetMapping(value = "/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDTO", new ItemFormDTO());
        return "item/itemForm";
    } // 구매대행 게시글 등록 컨트롤러

    @PostMapping(value = "/item/new")
    public String itemNew(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                          Principal principal) {

        // 폼에 대한 유효성 검사를 진행
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        // 첫 번째 이미지를 필수로 요구하는 조건
        if (itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            // 현재 로그인한 사용자의 이름을 seller로 설정
            itemFormDTO.setEmail(principal.getName());

            itemService.saveItem(itemFormDTO, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        // 상품 등록 후 아이템 리스트로 리다이렉트
        return "redirect:/item/itemList";
    }



    @GetMapping(value = "/item/{itemId}") // 수정
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDTO", itemFormDTO);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDTO", new ItemFormDTO());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDTO, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/item/itemList";
    }

    @GetMapping(value = {"/item/itemList", "/item/itemList/{page}"})  //페이징이 없는경우, 있는 경우
    public String itemManage(ItemSearchDTO itemSearchDTO, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        // 페이지 파라미터가 없으면 0번 페이지를 보임. 한 페이지당 3개의 상품만 보여줌.
        Page<Item> items = itemService.getAdminItemPage(itemSearchDTO, pageable);  // 조회 조건, 페이징 정보를 파라미터로 넘겨서 Page 타입으로 받음
        // 조회 조건과 페이징 정보를 파라미터로 넘겨서 item 객체 받음
        model.addAttribute("items", items); // 조회한 상품 데이터 및 페이징정보를 뷰로 전달
        model.addAttribute("itemSearchDto", itemSearchDTO); // 페이지 전환시 기존 검색 조건을 유지
        model.addAttribute("maxPage", 5);   // 상품관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수 5

        return "item/itemList";
        // itemMng.html로 리턴함.
    }

    // 상세페이지 조회
    @GetMapping(value = "/item/view/{itemId}")  // 경로를 명확하게 변경
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId, Principal principal) {
        ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);

        List<CommentDTO> comments = commentService.getCommentsByItemId(itemId);

        model.addAttribute("item", itemFormDTO);
        model.addAttribute("comments", comments);

        return "item/itemDtl";
    }


}
