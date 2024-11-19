package com.wywin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling // 스케줄링 활성화
public class WywinApplication {

    public static void main(String[] args) {
        SpringApplication.run(WywinApplication.class, args);
    }

}
