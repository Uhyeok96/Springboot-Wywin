package com.wywin.controller;

import com.wywin.dto.ItemSearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {    /*회원가입 후 메인 페이지로 갈 수 있도록 MainController 작성*/

    // 이미지가 저장된 경로
    private static final String IMAGE_DIRECTORY = "C:/wywin/auctionItem/"; // 경로 설정

    // 메인 페이지로 이동할 때 이미지 경로 설정
    @GetMapping(value = "/")
    public String main(Model model) {
        // 각 아이템에 맞는 이미지 경로 설정
        String auctionImg = "/images/biddingImg.png";   // 경매 아이템 이미지
        String sellingImg = "/images/shopImg.png";      // 판매 아이템 이미지
        String purchaseImg = "/images/purchaseImg.png"; // 구매대행 아이템 이미지

        // 모델에 이미지 경로 전달
        model.addAttribute("auctionImg", auctionImg);
        model.addAttribute("sellingImg", sellingImg);
        model.addAttribute("purchaseImg", purchaseImg);

        return "main"; // 메인 페이지로 이동
    }

    // 이미지 서빙하는 엔드포인트
    @GetMapping("/images/{imageName}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String imageName) {
        // C:/wywin/auctionItem/ 폴더에서 이미지를 찾음
        File imageFile = new File(IMAGE_DIRECTORY + imageName);
        if (imageFile.exists()) {
            FileSystemResource resource = new FileSystemResource(imageFile);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png") // 이미지 형식 설정 (png)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build(); // 파일이 존재하지 않으면 404 반환
        }
    }
}
