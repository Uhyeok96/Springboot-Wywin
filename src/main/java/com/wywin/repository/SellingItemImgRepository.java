package com.wywin.repository;

import com.wywin.entity.SellingItem;
import com.wywin.entity.SellingItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface SellingItemImgRepository extends JpaRepository<SellingItemImg, Long> {

    // 'id' 대신 'sid' 사용
    List<SellingItemImg> findBySellingItem_SidOrderBySidAsc(Long sellingId); // Sid를 기준으로 정렬

    SellingItemImg findBySellingItem_SidAndSrepimgYn(Long sellingId, String srepimgYn); // 구매이력 페이지에서 주문상품의 대표이미지를 보여줌

}
