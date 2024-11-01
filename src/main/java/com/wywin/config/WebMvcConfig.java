package com.wywin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // application.properties 파일 필수
    //#파일 한 개당 최대 사이즈
    //spring.servlet.multipart.maxFileSize=20MB
    //#요청당 최대 파일 크기
    //spring.servlet.multipart.maxRequestSize=100MB
    //#상품 이미지 업로드 경로
    //itemImgLocation=C:/shop/item
    //#리소스 업로드 경로
    //uploadPath=file:///C:/shop/
    //
    //#기본 batch size 설정
    //spring.jpa.properties.hibernate.default_batch_fetch_size=1000

    @Value("${uploadPath}") // application.properties에 설정한 "uploadPath" 프로퍼티 값을 읽어옴
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // service.FileService에 파일 처리용 클래스 생성
        registry.addResourceHandler("/images/**")   // 웹 브라우져에 입력하는 url에 /images로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일을 읽어오도록 설정
                .addResourceLocations(uploadPath);  // 로컬 컴퓨터에 저장된 파일을 읽어올 root 경로 설정

        // css 파일 경로 추가
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
    }
}
