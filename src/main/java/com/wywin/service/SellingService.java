package com.wywin.service;

import com.wywin.dto.ItemSearchDTO;
import com.wywin.dto.SellingItemFormDTO;
import com.wywin.dto.SellingItemImgDTO;
import com.wywin.entity.SellingItem;
import com.wywin.entity.SellingItemImg;
import com.wywin.repository.SellingItemImgRepository;
import com.wywin.repository.SellingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SellingService {

    private final SellingRepository sellingRepository;
    private final SellingItemImgService sellingItemImgService;
    private final SellingItemImgRepository sellingItemImgRepository;

    public Long saveSellingItem(SellingItemFormDTO sellingItemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {

        SellingItem sellingItem = sellingItemFormDTO.createItem();
        sellingRepository.save(sellingItem);

        for (int i = 0; i < itemImgFileList.size(); i++) {
            SellingItemImg sellingItemImg = new SellingItemImg();
            sellingItemImg.setSellingItem(sellingItem);
            sellingItemImg.setSrepimgYn(i == 0 ? "Y" : "N");

            sellingItemImgService.saveItemImg(sellingItemImg, itemImgFileList.get(i));
        }

        return sellingItem.getSid();
    }

    @Transactional(readOnly = true)
    public Page<SellingItem> getSellingItemPage(ItemSearchDTO itemSearchDto, Pageable pageable) {
        return sellingRepository.getSellingItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public SellingItemFormDTO getItemDtl(Long itemId) {
        SellingItem item = sellingRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        List<SellingItemImg> itemImgList = sellingItemImgRepository.findBySellingItem_SidOrderBySidAsc(itemId);
        List<SellingItemImgDTO> itemImgDtoList = itemImgList.stream()
                .map(SellingItemImgDTO::of)
                .collect(Collectors.toList());

        SellingItemFormDTO itemFormDto = SellingItemFormDTO.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(SellingItemFormDTO sellingItemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {
        SellingItem sellingItem = sellingRepository.findById(sellingItemFormDTO.getSid())
                .orElseThrow(EntityNotFoundException::new);

        sellingItem.updateItem(sellingItemFormDTO);

        List<Long> itemImgIds = sellingItemFormDTO.getItemImgIds();

        for (int i = 0; i < itemImgFileList.size(); i++) {
            if (i < itemImgIds.size() && itemImgFileList.get(i) != null && !itemImgFileList.get(i).isEmpty()) {
                sellingItemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
            }
        }

        return sellingItemFormDTO.getSid();
    }

    public SellingItem getSellingItem(Long sellingId) {
        return sellingRepository.findById(sellingId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public SellingItemFormDTO getSellingItemFormDTO(Long sellingId) {
        SellingItem sellingItem = sellingRepository.findById(sellingId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        SellingItemFormDTO sellingItemFormDTO = SellingItemFormDTO.of(sellingItem);

        List<SellingItemImgDTO> itemImgDtos = sellingItem.getSellingImgs().stream()
                .map(SellingItemImgDTO::of)
                .collect(Collectors.toList());
        sellingItemFormDTO.setItemImgDtoList(itemImgDtos);

        return sellingItemFormDTO;
    }

    @Transactional
    public void deleteItem(Long sellingId) {
        SellingItem sellingItem = sellingRepository.findById(sellingId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 상품이 존재하지 않습니다."));
        sellingRepository.delete(sellingItem); // 상품 삭제
    }

}
