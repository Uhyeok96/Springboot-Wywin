//package com.wywin.service;
//
//
//import com.wywin.entity.Member;
//import com.wywin.entity.MileageAccount;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class KakaoPayService {
//
//   // private final String KAKAO_PAY_URL = "https://kapi.kakao.com/v1/payment/ready";
//   // private final String REST_API_KEY = "DEVB467B9720DDC3F915E850FFD9ACD72AC7B745";
//
//    @Value("${kakao.pay.api.url}") // 카카오페이 API URL
//    private String kakaoPayApiUrl;
//
//    @Value("${kakao.pay.api.key}") // 카카오페이 API Key
//    private String kakaoPayApiKey;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public String createPaymentRequest(int amount) {
//        String url = kakaoPayApiUrl + "/v1/payment/ready"; // 결제 준비 API URL
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "KakaoAK " + kakaoPayApiKey);  // 카카오페이 API Key 설정
//        headers.set("Content-Type", "application/x-www-form-urlencoded");
//
//        // 결제 정보 설정 (결제 금액 등)
//        String body = "cid=TC0ONETIME&partner_order_id=12345&partner_user_id=user123&item_name=mileage&quantity=1&total_amount=" + amount
//                + "&tax_free_amount=0&approval_url=http://localhost:8080/payment-success&fail_url=http://localhost:8080/payment-fail&cancel_url=http://localhost:8080/payment-cancel";
//
//        HttpEntity<String> entity = new HttpEntity<>(body, headers);
//
//        // 카카오페이 결제 준비 요청
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//        return response.getBody();
//    }
//
//    public String verifyPayment(String pgToken) {
//        String url = kakaoPayApiUrl + "/v1/payment/approve";  // 결제 승인 API URL
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "KakaoAK " + kakaoPayApiKey);  // 카카오페이 API Key 설정
//        headers.set("Content-Type", "application/x-www-form-urlencoded");
//
//        // 결제 승인 요청에 필요한 정보 (결제 확인)
//        String body = "cid=TC0ONETIME&tid=12345&partner_order_id=12345&partner_user_id=user123&pg_token=" + pgToken;
//
//        HttpEntity<String> entity = new HttpEntity<>(body, headers);
//
//        // 카카오페이 결제 승인 요청
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//        return response.getBody();
//    }
//
//    // 결제 요청 (마일리지 차감 시 사용)
//    public String createPaymentRequestForDeduction(int amount) {
//        String url = kakaoPayApiUrl + "/v1/payment/ready"; // 카카오페이 결제 준비 API
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "KakaoAK " + kakaoPayApiKey);
//        headers.set("Content-Type", "application/x-www-form-urlencoded");
//
//        // 결제 준비 요청 파라미터 설정
//        String body = "cid=TC0ONETIME&partner_order_id=12345&partner_user_id=user123&item_name=mileage&quantity=1&total_amount=" + amount
//                + "&tax_free_amount=0&approval_url=http://localhost:8080/payment-success&fail_url=http://localhost:8080/payment-fail&cancel_url=http://localhost:8080/payment-cancel";
//
//        HttpEntity<String> entity = new HttpEntity<>(body, headers);
//
//        // 카카오페이 결제 준비 요청
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//        return response.getBody();  // 결제 요청 응답
//    }
//
//    // 결제 승인 (결제 성공 후 마일리지 차감)
//    public String verifyPaymentForDeduction(String pgToken) {
//        String url = kakaoPayApiUrl + "/v1/payment/approve";  // 카카오페이 결제 승인 API URL
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "KakaoAK " + kakaoPayApiKey);
//        headers.set("Content-Type", "application/x-www-form-urlencoded");
//
//        // 결제 승인 요청 파라미터 설정
//        String body = "cid=TC0ONETIME&tid=12345&partner_order_id=12345&partner_user_id=user123&pg_token=" + pgToken;
//
//        HttpEntity<String> entity = new HttpEntity<>(body, headers);
//
//        // 카카오페이 결제 승인 요청
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//        return response.getBody();  // 결제 승인 응답
//    }
//
//}
