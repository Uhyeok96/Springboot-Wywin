package com.wywin.service;

import com.wywin.constant.ItemStatus;
import com.wywin.dto.AuctionFormDTO;
import com.wywin.entity.AuctionImg;
import com.wywin.entity.AuctionItem;
import com.wywin.repository.AuctionImgRepository;
import com.wywin.repository.AuctionItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class AuctionItemServiceTest {

    @Autowired
    AuctionService auctionService;

    @Autowired
    AuctionItemRepository auctionItemRepository;

    @Autowired
    AuctionImgRepository auctionImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception{

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0;i<5;i++){
            String path = "C:/wywin/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "user1", roles = "USER")
    void saveItem() throws Exception {
        AuctionFormDTO auctionFormDTO = new AuctionFormDTO();
        auctionFormDTO.setItemName("테스트상품");
        auctionFormDTO.setItemStatus(ItemStatus.ONBID);
        auctionFormDTO.setItemDetail("테스트 상품 입니다.");
        auctionFormDTO.setBidPrice(1000);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = auctionService.saveItem(auctionFormDTO, multipartFileList);
        List<AuctionImg> auctionImgList = auctionImgRepository.findByItemIdOrderByIdAsc(itemId);

        AuctionItem auctionItem = auctionItemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(auctionFormDTO.getItemName(), auctionItem.getItemName());
        assertEquals(auctionFormDTO.getItemStatus(), auctionItem.getItemStatus());
        assertEquals(auctionFormDTO.getItemDetail(), auctionItem.getItemDetail());
        assertEquals(auctionFormDTO.getBidPrice(), auctionItem.getBidPrice());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), auctionImgList.get(0).getOriImgName());
    }
}
