package com.wywin.controller;

import com.wywin.dto.ItemSearchDTO;
import com.wywin.dto.SellingItemDTO;
import com.wywin.dto.SellingItemFormDTO;
import com.wywin.entity.SellingItem;
import com.wywin.service.SellingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SellingController {

    private final SellingService sellingService;


    // 판매 등록 폼 이동
    @GetMapping("/sellings/new")
    public String createSellingForm(Model model) {
        model.addAttribute("sellingItemFormDTO", new SellingItemFormDTO()); // SellingItemFormDTO로 수정
        return "sellings/sellingNew";
    }


    // 판매 등록
    @PostMapping("/sellings/new")
    public String createSelling(@Valid SellingItemFormDTO sellingItemFormDTO, BindingResult bindingResult,
                                Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        // 입력 검증 오류가 있는 경우 다시 폼을 반환
        if (bindingResult.hasErrors()) {
            return "sellings/sellingNew"; // 수정된 경로
        }

        // 첫 번째 이미지가 비어 있는 경우 오류 메시지 추가 후 폼 반환
        if (itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "sellings/sellingNew"; // 수정된 경로
        }

        try {
            // 상품 및 이미지 저장 처리
            sellingService.saveSellingItem(sellingItemFormDTO, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "sellings/sellingNew"; // 수정된 경로
        }

        // 성공적으로 등록된 경우 상품 목록 페이지로 리다이렉트
        return "redirect:/sellings/sellingList";
    }

    @GetMapping(value = {"/sellings/sellingList", "/sellings/sellingList/{page}"})  //페이징이 없는경우, 있는 경우
    public String itemManage(ItemSearchDTO itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        // 페이지 파라미터가 없으면 0번 페이지를 보임. 한 페이지당 3개의 상품만 보여줌.
        Page<SellingItem> items = sellingService.getSellingItemPage(itemSearchDto, pageable);  // 조회 조건, 페이징 정보를 파라미터로 넘겨서 Page 타입으로 받음
        // 조회 조건과 페이징 정보를 파라미터로 넘겨서 item 객체 받음
        model.addAttribute("items", items); // 조회한 상품 데이터 및 페이징정보를 뷰로 전달
        model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환시 기존 검색 조건을 유지
        model.addAttribute("maxPage", 5);   // 상품관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수 5

        return "sellings/sellinglist";
        // itemMng.html로 리턴함.
    }


}
