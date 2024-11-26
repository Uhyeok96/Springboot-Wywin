package com.wywin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MileageAccountDTO {
    private String accountNumber;  // 계좌 번호
    private double mileage;  // 보유 마일리지

    // 기본 생성자
    public MileageAccountDTO() {
    }

    public MileageAccountDTO(String accountNumber, double mileage) {
        this.accountNumber = accountNumber;
        this.mileage = mileage;
    }
}
