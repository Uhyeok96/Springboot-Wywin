package com.wywin.repository;

import com.wywin.entity.MileageAccount;
import com.wywin.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByMileageAccount(MileageAccount mileageAccount);
    // 계좌의 모든 트랜잭션 조회
}
