package com.wywin.dto;

import com.wywin.constant.ItemStatus;
import com.wywin.entity.Item;
import com.wywin.entity.Member;
import com.wywin.entity.SellingItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SellingItemDTO {

    private Long sid; // 판매번호

    private String sitemNm; // 판매 아이템 이름

    private int sprice; // 판매 가격

    private String itemDetail;  // 상품 상세 설명

    private int stockNumbers;   // 재고수량

    private String seller; // 판매자

    private String buyer; // 구매자

    private int quantity; // 구매 수량

    private LocalDateTime sellingDate; // 판매일

    private ItemStatus itemStatus; // 판매 상태

    private List<SellingItemImgDTO> itemImgDtoList = new ArrayList<>();    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트

    private List<Long> itemImgIds = new ArrayList<>();  // 상품의 이미지 아이디를 저장하는 리스트.

    // 232 추가 모델 처리를 위한 라이브러리 (DTO와 엔티티간의 변환 처리용) -> config.RootConfig에 적용
    // 상품을 등록할 때 화면으로 부터 전달 받은 DTO 객체를 엔티티로 변환하는 작업 대체(DTOtoEntity, EntityToDTO)
    // 서로다른 클래스의 값을 필드의 이름과 자료형이 같으면 getter, setter를 통해 값을 복사해서 객체로 변환 해줌)
    //    implementation 'org.modelmapper:modelmapper:3.1.0'
    private static ModelMapper modelMapper = new ModelMapper();

    public SellingItem createItem(){   // 엔티티 객체와 DTO 객체 간의 데이터를 복사하여 반환해주는 메서드
        return modelMapper.map(this, SellingItem.class);
    }

    public static SellingItemFormDTO of(SellingItem sellingItem){    // 엔티티 객체와 DTO 객체 간의 데이터를 복사하여 반환해주는 메서드
        return modelMapper.map(sellingItem, SellingItemFormDTO.class);
    }

}
