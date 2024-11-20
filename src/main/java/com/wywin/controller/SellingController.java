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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SellingController {

    private final SellingService sellingService;


    // 판매 등록 폼 이동
    @GetMapping("/sellings/new")
    public String createSellingForm(Model model) {
        model.addAttribute("sellingItemFormDTO", new SellingItemFormDTO());
        return "sellings/sellingNew";
    }


    // 판매 등록
    @PostMapping("/sellings/new")
    public String createSelling(@Valid SellingItemFormDTO sellingItemFormDTO, BindingResult bindingResult,
                                Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                                Principal principal) { // Principal 추가

        if (bindingResult.hasErrors()) {
            return "sellings/sellingNew";
        }

        if (itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "sellings/sellingNew";
        }

        try {
            // 현재 로그인한 사용자의 이름을 seller로 설정
            sellingItemFormDTO.setSeller(principal.getName());

            // 상품 및 이미지 저장 처리
            sellingService.saveSellingItem(sellingItemFormDTO, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다." + e.getMessage());
            return "sellings/sellingNew";
        }

        return "redirect:/sellings/sellingList";
    }

    // 상품 리스트
    @GetMapping(value = {"/sellings/sellingList", "/sellings/sellingList/{page}"})  //페이징이 없는경우, 있는 경우
    public String itemManage(ItemSearchDTO itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.orElse(0), 20);
        // 페이지 파라미터가 없으면 0번 페이지를 보임. 한 페이지당 3개의 상품만 보여줌.
        Page<SellingItem> items = sellingService.getSellingItemPage(itemSearchDto, pageable);  // 조회 조건, 페이징 정보를 파라미터로 넘겨서 Page 타입으로 받음
        // 조회 조건과 페이징 정보를 파라미터로 넘겨서 item 객체 받음
        model.addAttribute("items", items); // 조회한 상품 데이터 및 페이징정보를 뷰로 전달
        model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환시 기존 검색 조건을 유지
        model.addAttribute("maxPage", 5);   // 상품관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수 5

        return "sellings/sellinglist";
        // itemMng.html로 리턴함.
    }

    // 상세페이지 조회
    @GetMapping(value = "/sellings/view/{sellingId}")  // 경로를 명확하게 변경
    public String itemDtl(Model model, @PathVariable("sellingId") Long sellingId, Principal principal) {
        SellingItemFormDTO sellingItemFormDTO = sellingService.getItemDtl(sellingId);
        model.addAttribute("item", sellingItemFormDTO);

        SellingItem sellingItem = sellingService.getSellingItem(sellingId);
        boolean isUploader = false;
        if (principal != null && sellingItem.getSeller() != null) {
            isUploader = sellingItem.getSeller().equals(principal.getName());
        }
        model.addAttribute("isUploader", isUploader);

        return "sellings/sellingDtl";
    }

    // 상품 수정 페이지
    @GetMapping("/sellings/edit/{sellingId}")
    public String itemEdit(@PathVariable("sellingId") Long sellingId, Model model) {
        SellingItemFormDTO sellingItemFormDTO = sellingService.getSellingItemFormDTO(sellingId);
        model.addAttribute("sellingItemFormDTO", sellingItemFormDTO);
        return "sellings/sellingEdit";
    }

    // 상품 수정 페이지
    @PostMapping("/sellings/edit/{sellingId}")
    public String itemUpdate(@Valid SellingItemFormDTO sellingItemFormDTO, BindingResult bindingResult,
                             @RequestParam(value = "itemImgFile", required = false) List<MultipartFile> itemImgFileList,
                             Model model) {

        if(bindingResult.hasErrors()){
            return "sellings/sellingEdit";
        }

        // 첫 번째 이미지 필수 조건 체크 (상품 수정 시에도 필수일 수 있음)
        if(itemImgFileList == null || itemImgFileList.isEmpty()) {
            if(sellingItemFormDTO.getSid() == null) {
                model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
                return "sellings/sellingEdit";
            }
        }

        try {
            sellingService.updateItem(sellingItemFormDTO, itemImgFileList);   // 상품 수정 로직 호출
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "sellings/sellingEdit";
        }

        return "redirect:/sellings/sellingList"; // 수정 후 판매 목록으로 리다이렉트
    }

    @PostMapping("/sellings/delete/{sellingId}")
    public String deleteSellingItem(@PathVariable Long sellingId, RedirectAttributes redirectAttributes){
        try {
            sellingService.deleteItem(sellingId);
            redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "상품 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/sellings/sellingList"; // 삭제 후 목록 페이지로 리다이렉트
    }

}
