package com.wywin.service;

import com.wywin.dto.ItemSearchDTO;
import com.wywin.dto.SellingItemFormDTO;
import com.wywin.entity.SellingItem;
import com.wywin.entity.SellingItemImg;
import com.wywin.repository.SellingItemImgRepository;
import com.wywin.repository.SellingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SellingService{

    private final SellingRepository sellingRepository; // 리포지토리 의존성 주입
    private final SellingItemImgService sellingItemImgService;
    private final SellingItemImgRepository sellingItemImgRepository;

    public Long saveSellingItem(SellingItemFormDTO sellingItemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        SellingItem sellingItem = sellingItemFormDTO.createItem(); // 등록 폼에서 받은 데이터로 객체 생성
        sellingRepository.save(sellingItem);                               // DB에 저장

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            SellingItemImg sellingItemImg = new SellingItemImg();
            sellingItemImg.setSellingItem(sellingItem);

            if (i == 0)
                sellingItemImg.setSrepimgYn("Y");       // 첫 번째 이미지는 대표 이미지로 설정
            else
                sellingItemImg.setSrepimgYn("N");

            sellingItemImgService.saveItemImg(sellingItemImg, itemImgFileList.get(i)); // 상품 이미지 저장
        }

        return sellingItem.getSid();                    // 저장된 상품 ID 반환
    }

    @Transactional(readOnly = true)
    public Page<SellingItem> getSellingItemPage(ItemSearchDTO itemSearchDto, Pageable pageable){
        return sellingRepository.getSellingItemPage(itemSearchDto, pageable);
    } // 페이지 처리되는 아이템 처리용 (상품 조회 조건과 페이지 정보를 파라미터로 받아서 상품 데이터를 조회)



}
