package com.wywin.repository;

import com.wywin.entity.ItemLike;
import com.wywin.entity.Member;
import com.wywin.entity.SellingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemLikeRepository extends JpaRepository<ItemLike, Long> {

    // 특정 사용자가 특정 아이템에 대해 좋아요를 눌렀는지 확인
    Optional<ItemLike> findBySellingItemSidAndMemberId(Long sellingItemId, Long memberId);

    // 특정 아이템에 대한 좋아요 수 카운트
    Long countBySellingItemSid(Long sellingItemId);

    // 특정 사용자가 좋아요를 눌렀을 때 해당 좋아요 삭제
    void deleteBySellingItemSidAndMemberId(Long sellingItemId, Long memberId);

    // 특정 사용자가 아이템에 대해 좋아요를 눌렀을 때 추가
    ItemLike save(ItemLike itemLike);
}
