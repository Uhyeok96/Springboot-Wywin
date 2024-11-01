package com.wywin.dto;

import com.wywin.constant.ItemStatus;
import com.wywin.entity.SellingItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SellingItemFormDTO { // 프론트에서 넘어오는 객체 처리용

    private Long sid;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String sitemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer sprice;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String sitemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumbers;

    private ItemStatus itemStatus;


    private List<SellingItemImgDTO> itemImgDtoList = new ArrayList<>();    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트

    private List<Long> itemImgIds = new ArrayList<>();  // 상품의 이미지 아이디를 저장하는 리스트.

    private static ModelMapper modelMapper = new ModelMapper();

    public SellingItem createItem(){   // 엔티티 객체와 DTO 객체 간의 데이터를 복사하여 반환해주는 메서드
        return modelMapper.map(this, SellingItem.class);
    }

    public static SellingItemFormDTO of(SellingItem sellingItem){    // 엔티티 객체와 DTO 객체 간의 데이터를 복사하여 반환해주는 메서드
        return modelMapper.map(sellingItem, SellingItemFormDTO.class);
    }
}
