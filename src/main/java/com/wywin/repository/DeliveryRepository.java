package com.wywin.repository;

import com.wywin.constant.DeliveryStatus;
import com.wywin.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findByTrackingNumber(String trackingNumber);
    List<Delivery> findByDeliveryStatus(DeliveryStatus status);
}
