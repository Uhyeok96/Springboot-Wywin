package com.wywin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wywin.constant.CurrencyType;
import com.wywin.dto.AuctionItemDTO;
import com.wywin.dto.ExchangeRateDTO;
import com.wywin.dto.ExchangeRateResponseDTO;
import com.wywin.entity.AuctionItem;
import com.wywin.entity.ExchangeRate;
import com.wywin.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    private final ModelMapper modelMapper = new ModelMapper(); // DTO와 엔티티 간 변환을 위한 ModelMapper

    // application.properties에서 API URL을 읽어옴
    @Value("${exchange.api.url}")
    private String apiUrl;

    // 매일 정해진 시간에 환율 정보를 api를 통해 받아와 데이터베이스에 저장
    @Scheduled(cron = "0 15 11 * * *")
    @Transactional  // 트랜잭션을 적용하여 DB에 저장이 제대로 이루어지도록 보장
    public void updateExchangeRatesFromApi() {
        System.out.println("Scheduled task started at " + LocalDateTime.now());

        // API URL 확인 로그
        System.out.println("API URL: " + apiUrl);

        try {
            // RestTemplate을 사용하여 API 호출
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class);

            // HTTP 상태 코드 확인
            System.out.println("HTTP Status: " + responseEntity.getStatusCode());

            // API 응답을 그대로 출력 (JSON 형태)
            String responseBody = responseEntity.getBody();
            System.out.println("API Response (Raw JSON): " + responseBody);

            // 응답을 DTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ExchangeRateResponseDTO response = objectMapper.readValue(responseBody, ExchangeRateResponseDTO.class);

            // conversionRates가 정상적으로 들어오는지 확인
            if (response.getConversionRates() == null) {
                System.out.println("conversionRates is null. API response might be malformed or empty.");
            } else {
                System.out.println("Conversion Rates: " + response.getConversionRates());
            }

            if (response != null && "success".equals(response.getResult()) && response.getConversionRates() != null) {
                // 환율 정보 가져오기 (USD -> KRW, USD -> JPY)
                Double usdToKrw = response.getConversionRates().get("KRW");  // 수정된 부분
                Double usdToJpy = response.getConversionRates().get("JPY");  // 1달러에 대한 엔화 환율로 수정

                // 환율 정보가 null이 아니면 데이터베이스에 저장
                if (usdToKrw != null && usdToJpy != null) {
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setUsdToKrw(BigDecimal.valueOf(usdToKrw));  // Double -> BigDecimal 변환
                    exchangeRate.setUsdToJpy(BigDecimal.valueOf(usdToJpy));  // 1달러에 대한 엔화 환율 (usdToJpy)

                    // 저장
                    exchangeRateRepository.save(exchangeRate);
                    System.out.println("Exchange rates saved to the database.");
                } else {
                    System.out.println("USD or JPY conversion rates are missing.");
                }
            } else {
                System.out.println("No valid data received from the API. Response: " + response);
            }
        } catch (Exception e) {
            System.out.println("Error while fetching exchange rates: " + e.getMessage());
        }
    }

    // 최신 환율 정보를 DTO로 반환하는 메소드
    public ExchangeRateDTO getLatestExchangeRateDTO() {
        // 데이터베이스에서 가장 최근의 환율 정보를 가져옴
        ExchangeRate latestExchangeRate = exchangeRateRepository.findTopByOrderByRegTimeDesc();
        if (latestExchangeRate == null) {
            System.out.println("No exchange rate found in the database.");
            return null;
        }
        ExchangeRateDTO exchangeRateDTO = ExchangeRateDTO.fromEntity(latestExchangeRate);
        System.out.println("Loaded exchange rates: USD to KRW = " + exchangeRateDTO.getUsdToKrw() +
                ", USD to JPY = " + exchangeRateDTO.getUsdToJpy());
        return exchangeRateDTO;
    }

    // 입찰 상품 환율 계산 메서드
    public Integer calculateEstimatedPrice(AuctionItem auctionItem) {
        // 최신 환율 정보 가져오기
        ExchangeRateDTO exchangeRateDTO = getLatestExchangeRateDTO();

        if (auctionItem == null || exchangeRateDTO == null) {
            System.out.println("No auction item or exchange rate info found.");
            return 0;  // 경매 아이템이나 환율 정보가 없으면 0 반환
        }

        Integer finalPrice = auctionItem.getFinalPrice();  // 최종 낙찰가
        if (finalPrice == null || finalPrice == 0) {
            System.out.println("Final price is invalid: " + finalPrice);
            return 0;  // finalPrice가 없거나 0이면 0 반환
        }

        System.out.println("Final price (USD): " + finalPrice);  // 최종 가격 출력

        // 상품 가격이 USD일 때 (USD -> KRW 변환)
        if (auctionItem.getCurrencyType() == CurrencyType.USD) {
            BigDecimal usdToKrw = exchangeRateDTO.getUsdToKrw();  // USD -> KRW 환율
            if (usdToKrw == null) {
                System.out.println("USD to KRW exchange rate is null.");
                return 0;  // 환율 정보가 없으면 0 반환
            }

            System.out.println("USD to KRW exchange rate: " + usdToKrw);  // 환율 값 출력

            // 환율 계산: finalPrice * usdToKrw
            BigDecimal estimatedPrice = new BigDecimal(finalPrice).multiply(usdToKrw);
            System.out.println("USD to KRW conversion: " + finalPrice + " * " + usdToKrw + " = " + estimatedPrice);

            // 소수점 없이 1원 단위로 올림 처리 후 Integer로 변환하여 반환
            return estimatedPrice.setScale(0, RoundingMode.CEILING).intValue();
        }


        // 상품 가격이 JPY일 때 (JPY -> USD -> KRW 변환)
        else if (auctionItem.getCurrencyType() == CurrencyType.JPY) {
            BigDecimal usdToKrw = exchangeRateDTO.getUsdToKrw();  // USD -> KRW 환율
            BigDecimal usdToJpy = exchangeRateDTO.getUsdToJpy();  // USD -> JPY 환율

            if (usdToKrw == null || usdToJpy == null) {
                System.out.println("USD to KRW or USD to JPY exchange rate is null.");
                return 0;  // 환율 정보가 없으면 0 반환
            }

            System.out.println("USD to KRW exchange rate: " + usdToKrw);  // 환율 값 출력
            System.out.println("USD to JPY exchange rate: " + usdToJpy);  // 환율 값 출력

            // 환율 계산: JPY -> USD -> KRW
            BigDecimal estimatedPrice = new BigDecimal(finalPrice).multiply(usdToKrw).divide(usdToJpy, 2, RoundingMode.HALF_UP);
            System.out.println("JPY to USD to KRW conversion: " + finalPrice + " * " + usdToKrw + " / " + usdToJpy + " = " + estimatedPrice);

            // 소수점 없이 1원 단위로 올림 처리 후 Integer로 변환하여 반환
            return estimatedPrice.setScale(0, RoundingMode.CEILING).intValue();
        }

        // USD와 JPY가 아닌 경우, finalPrice 그대로 반환
        System.out.println("Currency type is neither USD nor JPY. Using final price directly.");
        return finalPrice;
    }




}
