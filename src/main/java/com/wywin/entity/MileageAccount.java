package com.wywin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MileageAccount {

    @Id
    private String accountNumber;  // 계좌 번호
    private String owner;  // 계좌 주인
    private double mileage;  // 보유 마일리지


}
