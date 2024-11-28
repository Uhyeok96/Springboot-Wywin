package com.wywin.exception;

public class InsufficientFundsException  extends RuntimeException {

    // 기본 생성자
    public InsufficientFundsException(String message) {
        super(message); // 예외 메시지를 부모 클래스인 RuntimeException에 전달
    }
}
