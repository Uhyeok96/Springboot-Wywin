package com.wywin.exception;

public class AuctionItemNotFoundException extends RuntimeException{
    // 경매 아이템을 찾을 수 없을 때 발생한 예외를 처리합니다.

    public AuctionItemNotFoundException(String message) {
        super(message); // 예외 메시지 전달
    }

    public AuctionItemNotFoundException(String message, Throwable cause) {
        super(message, cause); // 예외 메시지와 원인 전달
    }
}
