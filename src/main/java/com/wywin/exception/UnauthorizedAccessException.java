package com.wywin.exception;

public class UnauthorizedAccessException extends RuntimeException{
    // 권한이 없을 때 발생한 예외를 처리합니다.

    public UnauthorizedAccessException(String message) {
        super(message); // 예외 메시지 전달
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause); // 예외 메시지와 원인 전달
    }
}
