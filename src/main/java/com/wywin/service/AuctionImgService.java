package com.wywin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AuctionImgService {
    // 컨트롤러의 책임을 줄이고, 이미지 저장 로직이 독립적으로 관리되어 코드의 가독성과 유지보수성이 높아지도록 서비스 구현

    private final String directoryPath = "C:\\wywin\\auctionItem"; // 파일 저장 경로

    public String saveImageFile(MultipartFile imageFile) {
        File directory = new File(directoryPath);

        // 디렉토리가 존재하지 않으면 생성
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // UUID를 사용하여 새로운 파일명 생성
        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
        String imgName = UUID.randomUUID() + fileExtension; // 새로운 파일명 생성

        // 저장할 파일의 경로 설정
        Path filePath = Paths.get(directoryPath, imgName);

        try {
            // 파일을 지정한 경로에 저장
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
            // 에러 처리를 추가할 수 있음 (예: 사용자에게 에러 메시지 표시)
            return null; // 실패 시 null 반환
        }

        // URL 경로 반환 (예: /images/auction/UUID.확장자)
        return "/images/auction/" + imgName;
    }
}
