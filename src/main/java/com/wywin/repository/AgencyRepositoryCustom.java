package com.wywin.repository;

import com.wywin.dto.ItemSearchDTO;
import com.wywin.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepositoryCustom {

    Page<Item> getItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);
}
