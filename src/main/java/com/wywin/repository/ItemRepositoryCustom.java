package com.wywin.repository;

import com.wywin.dto.ItemSearchDTO;
import com.wywin.entity.SellingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<SellingItem> getSellingItemPage(ItemSearchDTO itemSearchDto, Pageable pageable);
}
