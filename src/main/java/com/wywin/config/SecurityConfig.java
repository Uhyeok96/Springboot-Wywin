package com.wywin.config;

import com.wywin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 설정 클래스로 선언
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) // 스프링 시큐리티 필터가 스프링 필터체인에 등록
public class SecurityConfig {

    private final MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // filterChain : 특정 HTTP 요청에 대해 웹 기반 보안 구성. 인증/인가 및 로그인,로그아웃 설정
        http
                .formLogin(form -> {
                    form
                            .loginPage("/members/login") // 로그인 페이지 설정
                            .defaultSuccessUrl("/") // 로그인 성공시 기본 경로
                            .usernameParameter("email") // 로그인 인증 키값
                            .failureUrl("/members/login/error"); // 실패시 이동 페이지
                })
                .logout(logout -> {
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 처리용 경로
                            .logoutSuccessUrl("/"); // 로그아웃 성공 시 갈 경로
                });

        // authorizeHttpRequests - 스프링 시큐리티의 구성 메서드 내에서 사용되는 메서드 | HTTP 요청에 대한 인가 설정 구성
        // 이 메서드를 사용해 다양한 인가 규칙을 정의할 수 있음, 경로별 다른 권한 설정 가능
        http.authorizeHttpRequests(authorizeHttpRequests -> {
            authorizeHttpRequests
                    // requestMatchers - HTTP 요청매체를 적용
                    // .permitAll() 모든 요청을 인가(인증된 사용자 권한에 상관없음)
                    .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                    .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    // admin 하위 메서드는 ADMIN 룰에 적용됨
                    .anyRequest().authenticated();
            // requestMatchers : 특정 요청과 일치하는지 url에 대한 엑세스 설정
        });

        http.csrf(httpSecurityCsrfConfigurer ->  httpSecurityCsrfConfigurer.disable() );

        http.exceptionHandling(exceptionHandling -> {
            exceptionHandling
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        });

        return http.build();
    }



}
