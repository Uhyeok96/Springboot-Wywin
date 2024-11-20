package com.wywin.repository;

import com.wywin.entity.SellingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SellingRepository extends JpaRepository<SellingItem, Long>, QuerydslPredicateExecutor<SellingItem>, ItemRepositoryCustom{

    List<SellingItem> findBySitemNm(String sitemNm);
}
