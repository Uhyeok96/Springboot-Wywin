package com.wywin.service;

import com.wywin.entity.AuctionImg;
import com.wywin.repository.AuctionImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AuctionImgService {
    // 컨트롤러의 책임을 줄이고, 이미지 저장 로직이 독립적으로 관리되어 코드의 가독성과 유지보수성이 높아지도록 서비스 구현

    private final String directoryPath = "C:\\wywin\\auctionItem"; // 파일 저장 경로

    @Autowired
    private AuctionImgRepository auctionImgRepository; // 이미지 관련 DB 작업을 위한 Repository

    // 이미지 등록
    public String saveImageFile(MultipartFile imageFile) {
        File directory = new File(directoryPath);

        // 디렉토리가 존재하지 않으면 생성
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 원본 파일명과 확장자 추출
        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
        String imgName = UUID.randomUUID() + fileExtension; // UUID로 새로운 파일명 생성

        // 저장할 파일의 경로 설정
        Path filePath = Paths.get(directoryPath, imgName);

        try {
            // 파일을 지정한 경로에 저장
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 실패 시 null 반환
        }

        // URL 경로 반환 (예: /images/auction/UUID.확장자)
        return "/images/auction/" + imgName;
    }

    // 이미지 삭제 메서드
    public void deleteImage(AuctionImg auctionImg) {
        // 로컬 디스크에서 파일 삭제
        String imagePath = auctionImg.getImgUrl().replace("/images/auction/", directoryPath + "\\");

        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            imageFile.delete(); // 파일 삭제
        }

        // DB에서 이미지 레코드 삭제
        auctionImgRepository.delete(auctionImg);
    }

    // 여러 이미지를 삭제할 때 호출되는 메서드
    public void deleteImages(List<AuctionImg> auctionImgs) {
        for (AuctionImg img : auctionImgs) {
            deleteImage(img); // 각각의 이미지를 삭제
        }
    }

}