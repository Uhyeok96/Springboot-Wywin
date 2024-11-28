package com.wywin.service;


import com.wywin.entity.Member;
import com.wywin.entity.MileageAccount;
import com.wywin.repository.MemberRepository;
import com.wywin.repository.MileageAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MileageAccountService {

    @Autowired
    private MileageAccountRepository mileageAccountRepository;
    @Autowired
    private MemberRepository memberRepository;



    // 마일리지 충전
    public void chargeMileage(Long accountId, int amount) {
        MileageAccount account = mileageAccountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.addMileage(amount);
        mileageAccountRepository.save(account);
    }

    // 마일리지 차감
    public boolean deductMileage(Long accountId, int amount) {
        MileageAccount account = mileageAccountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        return account.deductMileage(amount);
    }

    // 마일리지 송금
   /* public boolean transferMileage(Long senderId, Long receiverId, int amount) {
        // 송금하는 회원과 받는 회원을 찾는다
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // 송금하는 회원의 마일리지 차감
        MileageAccount senderAccount = sender.getMileageAccount();
        boolean deductionSuccess = senderAccount.deductMileage(amount);  // 차감 성공 여부
        if (!deductionSuccess) {
            return false;  // 차감 실패하면 false 반환
        }

        // 받는 회원의 마일리지 충전
        MileageAccount receiverAccount = receiver.getMileageAccount();
        receiverAccount.addMileage(amount);  // 충전

        // 업데이트된 계좌 저장
        mileageAccountRepository.save(senderAccount);
        mileageAccountRepository.save(receiverAccount);

        return true;  // 송금 성공
    }*/

}
