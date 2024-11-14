package com.wywin.exception;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Spring Boot에서 전역적인 예외를 처리하는 클래스
    // @ControllerAdvice를 사용하여 애플리케이션 전역에서 발생할 수 있는 예외를 잡고, 적절한 응답을 반환

    // RuntimeException 처리
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
        return "error";  // error.html로 이동
    }

    // 모든 예외를 처리하는 메서드
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        model.addAttribute("errorMessage", "오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return "error";  // 공통 에러 페이지로 이동
    }
}
