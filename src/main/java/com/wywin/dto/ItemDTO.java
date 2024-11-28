package com.wywin.dto;

import com.wywin.constant.ItemStatus;
import com.wywin.entity.Item;
import com.wywin.entity.SellingItem;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ItemDTO {
    private Long id;

    private String title;

    private String itemNm;

    private String email;

    private String content;

    private ItemStatus itemStatus;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){   // 엔티티 객체와 DTO 객체 간의 데이터를 복사하여 반환해주는 메서드
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDTO of(Item item){    // 엔티티 객체와 DTO 객체 간의 데이터를 복사하여 반환해주는 메서드
        return modelMapper.map(item, ItemFormDTO.class);
    }
}
