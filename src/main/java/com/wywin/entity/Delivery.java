package com.wywin.entity;

import com.wywin.constant.DeliveryStatus;
import com.wywin.dto.DeliveryDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    private String trackingNumber; // 운송장 번호

    private String recipientName;  // 수취인 이름

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus; // 배송 상태 (ENUM 타입으로 관리)

    public static Delivery createDelivery(DeliveryDTO deliveryDTO) {
        Delivery delivery = new Delivery();
        delivery.setTrackingNumber(deliveryDTO.getTrackingNumber());
        delivery.setRecipientName(deliveryDTO.getRecipientName());
        delivery.setDeliveryStatus(DeliveryStatus.PENDING);

        return delivery;
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
