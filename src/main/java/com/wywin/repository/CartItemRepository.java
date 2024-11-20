package com.wywin.repository;

import com.wywin.dto.CartDetailDTO;
import com.wywin.entity.Cart;
import com.wywin.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndSellingItemSid(Long cartId, Long itemId); // 카트아이다와 상품 아이디를 이용해서 장바구니에 있는지 조회

    @Query("select new com.wywin.dto.CartDetailDTO(ci.id, si.sitemNm, si.sprice, ci.count, im.simgUrl) " +
            "from CartItem ci " +
            "join ci.sellingItem si " + // sellingItem을 참조
            "join SellingItemImg im on im.sellingItem.sid = si.sid " + // ItemImg와 SellingItem 관계로 조인
            "where ci.cart.id = :cartId " +
            "and im.srepimgYn = 'Y' " +
            "order by ci.regTime desc")
        // 생성자를 이용하여 DTO를 반환할 때는 new 키워드와 해당 DTO의 패키지, 클래스명을 적음
    List<CartDetailDTO> findCartDetailDTOList(Long cartId);
    // 장바구니 페이지에 전달할 리스트를 쿼리문
}
