package com.wywin.repository;

import com.wywin.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    // 가장 최근의 환율 정보를 가져오는 메서드
    ExchangeRate findTopByOrderByRegTimeDesc();
}
