package com.wywin.service;

import com.wywin.entity.SellingItemImg;
import com.wywin.repository.SellingItemImgRepository;
import com.wywin.repository.SellingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class SellingItemImgService {

    @Value("${itemImgLocation}")    // application.properties에 등록한 "#상품 이미지 업로드 경로 itemImgLocation=C:/shop/item" 불러옴
    private String itemImgLocation;

    private final SellingItemImgRepository sellingItemImgRepository;

    private final FileService fileService;

    // 상품 이미지 저장
    public void saveItemImg(SellingItemImg sellingItemImg, MultipartFile itemImgFile) throws Exception {
        String soriImgName = itemImgFile.getOriginalFilename();
        String simgName = "";
        String simgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(soriImgName)) {
            simgName = fileService.uploadFile(itemImgLocation, soriImgName, itemImgFile.getBytes());
            // 사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메서드 호출
            simgUrl = "/images/item/" + simgName;
            // 저장한 상품의 이미지 불러올 경로를 설정 WebMvcConfig 에서 설정함 c:\shop이므로 /images/item/를 붙여줌
        }

        // 상품 이미지 정보 저장
        sellingItemImg.updateItemImg(soriImgName, simgName, simgUrl);
        // imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름, oriImgName : 업로드했던 상품 이미지 파일의 원래 이름, imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로

        sellingItemImgRepository.save(sellingItemImg);  // 주입된 인스턴스를 통해 save 호출
    }
}
