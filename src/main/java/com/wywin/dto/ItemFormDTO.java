package com.wywin.dto;

import com.wywin.constant.ItemStatus;
import com.wywin.entity.Item;
import com.wywin.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDTO {
    // 프론트에서 넘어오는 객체 처리용

    private Long id; // 게시글 ID (수정 시 필요)

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 50, message = "제목은 최대 50자까지 입력 가능합니다.")
    private String title; // 게시글 제목

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    @Size(max = 50, message = "상품명은 최대 50자까지 입력 가능합니다.")
    private String itemNm; // 상품명

    private String email; // 등록이메일

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = 100, message = "내용은 최대 100자까지 입력 가능합니다.")
    private String content; // 게시글 내용

    private ItemStatus itemStatus; // 상품 상태

    private String comments; // 댓글

    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트

    private List<Long> itemImgIds = new ArrayList<>();
    // 상품의 이미지 아이디를 저장하는 리스트

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    } // modelMapper를 이용하여 엔티티 객체와 DTO객체 간의 데이터를 복사하여 복사한 개체를 반환해주는 메소드

    public static ItemFormDTO of(Item item){
        return modelMapper.map(item, ItemFormDTO.class);
    }

}
