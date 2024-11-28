package com.wywin.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
public class MileageAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 마일리지 계좌 ID

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member ;  // 계좌 주인

    private double mileage;  // 보유 마일리지

    @Builder.Default
    private int balance=1000; // 마일리지 잔액

    // 기본생성자
    public MileageAccount() {
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

    /* 마일리지 충전 메서드*/
    public void addMileage(int amount) {
        this.balance += amount; // 잔액 증가
    }

    // 마일리지 차감 메서드
    public boolean deductMileage(int amount) {
        if (this.balance >= amount) {
            this.balance -= amount;  // 잔액 차감
            return true;
        }
        return false;  // 잔액 부족 시 차감 실패
    }



}
