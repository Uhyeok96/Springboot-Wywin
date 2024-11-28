package com.wywin.service;

import com.wywin.dto.ItemFormDTO;
import com.wywin.dto.ItemImgDTO;
import com.wywin.dto.ItemSearchDTO;
import com.wywin.entity.Item;
import com.wywin.entity.ItemImg;
import com.wywin.repository.ItemImgRepository;
import com.wywin.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDTO.createItem();   // 등록 폼으로 입력 받은 데이터를 이용해 객체 생성
        itemRepository.save(item);              // db에 저장

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepimgYn("Y");        // 이미지가 첫번째 일 경우 대표이미지 Y 처리
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 상품 이미지 저장
        }

        return item.getId();                // 저장된 id를 리턴
    }

    @Transactional(readOnly = true) // 상품을 읽어오는 트랜젝션을 읽기 전용으로 설정하면 성능이 개선됨.(더티체킹(변경감지) 수행않음)
    public ItemFormDTO getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);
            itemImgDTOList.add(itemImgDTO);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
        itemFormDTO.setItemImgDTOList(itemImgDTOList);
        log.info("item : " + item);
        return itemFormDTO;
    }

    public Long updateItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDTO.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDTO);
        List<Long> itemImgIds = itemFormDTO.getItemImgIds();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable){
        return itemRepository.getItemPage(itemSearchDTO, pageable);
    } // 페이지 처리되는 아이템 처리용

}
