package com.wywin.service;

import com.wywin.entity.ItemLike;
import com.wywin.entity.Member;
import com.wywin.entity.SellingItem;
import com.wywin.repository.ItemLikeRepository;
import com.wywin.repository.MemberRepository;
import com.wywin.repository.SellingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemLikeService {

    @Autowired
    private ItemLikeRepository itemLikeRepository;

    @Autowired
    private SellingRepository sellingRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 특정 사용자가 특정 아이템에 좋아요를 눌렀는지 확인하고, 눌렀다면 삭제, 그렇지 않으면 추가
    @Transactional
    public void toggleLike(Long sellingId, Long memberId) {
        // 이미 좋아요를 눌렀다면 삭제
        ItemLike existingLike = itemLikeRepository.findBySellingItemSidAndMemberId(sellingId, memberId)
                .orElse(null);
        if (existingLike != null) {
            itemLikeRepository.deleteBySellingItemSidAndMemberId(sellingId, memberId); // 좋아요 삭제
        } else {
            // 좋아요가 없으면 새로운 좋아요 추가
            ItemLike newLike = new ItemLike(sellingId, memberId);
            itemLikeRepository.save(newLike); // 좋아요 추가
        }
    }

    // 특정 아이템에 대한 좋아요 수를 반환
    public Long getLikeCount(Long itemId) {
        return itemLikeRepository.countBySellingItemSid(itemId);
    }
}
