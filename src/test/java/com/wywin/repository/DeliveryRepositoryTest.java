package com.wywin.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class DeliveryRepositoryTest {



    @Test
    @DisplayName("배달 상품 테스트")
    public void createDeliveryTest(){

    }

}