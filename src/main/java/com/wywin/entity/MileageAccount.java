package com.wywin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class MileageAccount {

    @Id
    private String accountNumber;  // 계좌 번호

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member ;  // 계좌 주인

    private double mileage;  // 보유 마일리지

    // 기본생성자
    public MileageAccount() {
    }

    public MileageAccount(String accountNumber, Member member, double mileage) {
        this.accountNumber = accountNumber;
        this.member = member;
        this.mileage = mileage;
    }

    // 마일리지 충전
    public void chargeMileage(double amount) {
        if (amount > 0) {
            this.mileage += amount;
        }
    }

    // 마일리지 사용
    public void useMileage(double amount) {
        System.out.println("현재 마일리지: " + this.mileage + ", 차감 요청: " + amount);
        if (this.mileage < amount) {
            throw new IllegalArgumentException("잔액 부족");
        }
        this.mileage -= amount; // 차감
        System.out.println("차감 후 마일리지: " + this.mileage);
        /*if (amount > 0 && this.mileage >= amount) {
            this.mileage -= amount;
        }*/
    }



}
