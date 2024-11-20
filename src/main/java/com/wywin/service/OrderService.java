package com.wywin.service;

import com.wywin.dto.OrderDTO;
import com.wywin.dto.OrderHistDTO;
import com.wywin.dto.OrderItemDTO;
import com.wywin.entity.*;
import com.wywin.repository.MemberRepository;
import com.wywin.repository.OrderRepository;
import com.wywin.repository.SellingItemImgRepository;
import com.wywin.repository.SellingRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final SellingRepository sellingRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final SellingItemImgRepository sellingItemImgRepository;

    public Long order(OrderDTO orderDTO, String email){
        SellingItem sellingItem = sellingRepository.findById(orderDTO.getSellingId()).orElseThrow(EntityExistsException::new);
        // 주문할 상품을 조회
        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원의 이메일 정보를 이용해서 회원정보를 조회

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(sellingItem, orderDTO.getCount()); // 주문할 상품 엔티티와 주문 수량을 이용하여 주문상품 엔티티를 생성
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList); // 회원정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티를 생성
        orderRepository.save(order); // 생성한 주문 엔티티를 저장

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDTO> getOrderList(String email, Pageable pageable){
        // 주문 목록을 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDTO> orderHistDTOS = new ArrayList<>();

        for(Order order : orders){
            OrderHistDTO orderHistDTO = new OrderHistDTO(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems){
                SellingItemImg sellingItemImg = sellingItemImgRepository.findBySellingItem_SidAndSrepimgYn(orderItem.getSellingItem().getSid(), "Y");
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem, sellingItemImg.getSimgUrl());
                orderHistDTO.addOrderItemDTO(orderItemDTO);
            }
            orderHistDTOS.add(orderHistDTO);
        }
        return new PageImpl<OrderHistDTO>(orderHistDTOS, pageable, totalCount);
    }

    @Transactional(readOnly = true) // 주문 취소하는 로직
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityExistsException::new);
        Member saveMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())){
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityExistsException::new);
        order.cancelOrder();
    }

    // 장바구니에서 상품 주문
    public Long orders(List<OrderDTO> orderDtoList, String email){
        // 361 추가
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDTO orderDTO : orderDtoList) {    // 주문할 상품 리스트를 만듬
            SellingItem sellingItem = sellingRepository.findById(orderDTO.getSellingId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(sellingItem, orderDTO.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList); // 현재 로그인한 회원과 주문 상품 목록을 이용하여 주문 엔티티 생성
        orderRepository.save(order);    // 주문 데이터 저장

        return order.getId();
    }
}
