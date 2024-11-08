package com.wywin.dto;

import com.wywin.entity.AuctionImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class AuctionImgDTO {

    private Long id;    // 이미지 ID

    private String imgName; // 이미지 파일명 (uuid로 저장)

    private String oriImgName; // 원본 이미지 파일명

    private String imgUrl; // 이미지 조회 경로

    private String repimgYn; // 대표 이미지 여부

    private static ModelMapper modelMapper = new ModelMapper(); // 맴버 변수로 객체 추가

    public static AuctionImgDTO of(AuctionImg auctionImg) {    // AuctionImg를 파라미터로 받아서 자료형과 변수이름이 같을 때 AuctionImgDTO로 값을 복사해 반환
        return modelMapper.map(auctionImg,AuctionImgDTO.class);
    }
}
