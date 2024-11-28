package com.wywin.dto;


import com.wywin.constant.DeliveryStatus;

public class DeliveryDTO {


    private Long id;


    private String trackingNumber; // 운송장 번호

    private String recipientName;  // 수취인 이름


    private DeliveryStatus deliveryStatus; // 배송 상태 (ENUM 타입으로 관리)

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
