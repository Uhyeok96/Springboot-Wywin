package com.wywin.service;

import com.wywin.dto.CartDetailDTO;
import com.wywin.dto.CartItemDTO;
import com.wywin.dto.CartOrderDTO;
import com.wywin.dto.OrderDTO;
import com.wywin.entity.Cart;
import com.wywin.entity.CartItem;
import com.wywin.entity.Member;
import com.wywin.entity.SellingItem;
import com.wywin.repository.CartItemRepository;
import com.wywin.repository.CartRepository;
import com.wywin.repository.MemberRepository;
import com.wywin.repository.SellingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final SellingRepository sellingRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final OrderService orderService;

    public Long addCart(CartItemDTO cartItemDTO, String email){
        SellingItem sellingItem = sellingRepository.findById(cartItemDTO.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndSellingItemSid(cart.getId(), sellingItem.getSid());

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDTO.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, sellingItem, cartItemDTO.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    public List<CartDetailDTO> getCartList(String email) {
        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            return cartDetailDTOList;  // 회원이 없으면 빈 리스트 반환
        }

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            return cartDetailDTOList;  // 장바구니가 없으면 빈 리스트 반환
        }

        cartDetailDTOList = cartItemRepository.findCartDetailDTOList(cart.getId());
        System.out.println(cartDetailDTOList);  // 장바구니 아이템이 잘 조회되었는지 확인

        return cartDetailDTOList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        Member curMember = memberRepository.findByEmail(email);  // 현재 로그인한 사용자
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();  // 장바구니 상품을 저장한 회원 조회

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {  // 장바구니 상품 삭제
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDTO> cartOrderDtoList, String email){ // 361 추가
        List<OrderDTO> orderDtoList = new ArrayList<>();

        for (CartOrderDTO cartOrderDto : cartOrderDtoList) {    // 장바구니 페이지에서 전달받은 주문 상품 번호를 이용하여 주문 로직으로 전달할 orderDto 객체 생성
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setSellingId(cartItem.getSellingItem().getSid());
            orderDTO.setCount(cartItem.getCount());
            orderDtoList.add(orderDTO);
        }

        Long orderId = orderService.orders(orderDtoList, email);    // 장바구니에 담은 상품을 주문하도록 주문 로직 호출

        for (CartOrderDTO cartOrderDto : cartOrderDtoList) {    // 주문 완료한 상품들은 장바구니에서 제거해야함
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }
}
