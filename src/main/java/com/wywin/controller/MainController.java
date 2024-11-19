package com.wywin.controller;

import com.wywin.dto.ItemSearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {    /*회원가입 후 메인 페이지로 갈 수 있도록 MainController 작성*/

    @GetMapping(value = "/")
    public String main(ItemSearchDTO itemSearchDTO, Optional<Integer> page, Model model) {
    /*     Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDTO> items = itemService.getMainItemPage(itemSearchDTO, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDTO);
        model.addAttribute("maxPage", 5);
       */
        return "main";
    }
}
