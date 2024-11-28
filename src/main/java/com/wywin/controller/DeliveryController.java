package com.wywin.controller;

import com.wywin.constant.DeliveryStatus;
import com.wywin.entity.Delivery;
import com.wywin.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;


    // 배송 조회 페이지
    @GetMapping("/search")
    public String showSearchForm() {
        return "deliveries/delivery-status"; // delivery-status.html을 반환
    }

    /*// 배송 정보 조회 (운송장 번호로)
    @GetMapping("/search")
    public String getDeliveryByTrackingNumber(@RequestParam String trackingNumber, Model model) {
        Delivery delivery = deliveryService.getDeliveryByTrackingNumber(trackingNumber);
        model.addAttribute("delivery", delivery);
        return "deliveries/delivery-status";
    }*/

    // 배송 상태 변경
    @PostMapping("/{trackingNumber}/status")
    public String updateDeliveryStatus(@PathVariable String trackingNumber,
                                       @RequestParam DeliveryStatus status,
                                       Model model) {
        Delivery updatedDelivery = deliveryService.updateDeliveryStatus(trackingNumber, status);
        model.addAttribute("delivery", updatedDelivery);
        return "deliveries/delivery-status";
    }

    // 배송 상태별 목록 조회
    @GetMapping("/status")
    public String getDeliveriesByStatus(@RequestParam DeliveryStatus status, Model model) {
        List<Delivery> deliveries = deliveryService.getDeliveriesByStatus(status);
        model.addAttribute("deliveries", deliveries);
        return "deliveries/delivery-list"; // delivery-list.html을 반환
    }
}



