package com.wywin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Spring Boot에서 전역적인 예외를 처리하는 클래스
    // @ControllerAdvice를 사용하여 애플리케이션 전역에서 발생할 수 있는 예외를 잡고, 적절한 응답을 반환

    // 경매 아이템을 찾을 수 없을 때 발생한 예외를 처리합니다.
    @ExceptionHandler(AuctionItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // HTTP 404 상태 코드
    public String handleAuctionItemNotFound(AuctionItemNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage()); // 예외 메시지 모델에 추가
        return "error/auctionItemNotFound"; // 사용자에게 보여줄 에러 페이지
    }

    // 권한이 없을 때 발생한 예외를 처리합니다.
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // HTTP 403 상태 코드
    public String handleUnauthorizedAccess(UnauthorizedAccessException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage()); // 예외 메시지 모델에 추가
        return "error/unauthorized"; // 사용자에게 보여줄 에러 페이지
    }

    // 모든 예외를 잡아서 처리하는 기본 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500 상태 코드
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return "error/generalError"; // 일반적인 에러 페이지
    }
}
