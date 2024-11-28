package com.wywin.controller;

import com.wywin.constant.DeliveryStatus;
import com.wywin.dto.DeliveryDTO;
import com.wywin.dto.MemberDTO;
import com.wywin.entity.Delivery;
import com.wywin.entity.Member;
import com.wywin.repository.DeliveryRepository;
import com.wywin.service.DeliveryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@SpringBootTest
public class DeliveryControllerTest {

    @Autowired
    private DeliveryRepository deliveryRepository;
    private DeliveryService deliveryService;

    public Delivery test() throws Exception {
        // 초기 데이터 추가
        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setTrackingNumber("123456");
        deliveryDTO.setRecipientName("홍길동");
        deliveryDTO.setDeliveryStatus(DeliveryStatus.PENDING);

        return Delivery.createDelivery(deliveryDTO);
    }

    @Test
    public void saveDelivery() throws Exception {
        Delivery delivery = test();
        Delivery saveDelivery = deliveryRepository.save(delivery);
    }
}
