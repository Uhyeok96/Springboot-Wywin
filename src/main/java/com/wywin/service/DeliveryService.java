package com.wywin.service;


import com.wywin.constant.DeliveryStatus;
import com.wywin.entity.Delivery;
import com.wywin.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    // 운송장 번호로 배송 정보 조회
    public Delivery getDeliveryByTrackingNumber(String trackingNumber) {
        return deliveryRepository.findByTrackingNumber(trackingNumber);
    }

    // 배송 상태 변경
    public Delivery updateDeliveryStatus(String trackingNumber, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findByTrackingNumber(trackingNumber);
        if (delivery != null) {
            delivery.setDeliveryStatus(status);
            return deliveryRepository.save(delivery);
        }
        return null;
    }

    // 특정 상태의 배송 리스트 조회
    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) {
        return deliveryRepository.findByDeliveryStatus(status);
    }
}


