package com.festimap.tiketing.infra;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@Disabled
public class RestTemplateTest {

    @Autowired
    @Qualifier("simpleRestTemplate")
    private RestTemplate simpleTemplate;

    @Autowired
    @Qualifier("pooledRestTemplate")
    private RestTemplate poolTemplate;

    private final String url = "http://httpbin.org/get";

    @Test
    void RestTemplate_멀티스레드_테스트() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10); // 스레드 동기화용

        for (int i = 0; i < 10; i++) {
            int threadNumber = i + 1;
            executor.submit(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    String response = simpleTemplate.getForObject(url, String.class);
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    System.out.println(threadNumber+"번 전송 소요 시간: " + duration + "ms");

                } catch (Exception e) {
                    System.err.println("Thread #" + threadNumber + " failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
            Thread.sleep(2000);
        }

        latch.await(); // 모든 스레드 종료 대기
        executor.shutdown();
        System.out.println("모든 요청 완료");
    }
}
