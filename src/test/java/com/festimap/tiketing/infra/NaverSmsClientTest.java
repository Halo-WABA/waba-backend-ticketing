package com.festimap.tiketing.infra;

import com.festimap.tiketing.infra.sms.SmsClient;
import com.festimap.tiketing.infra.sms.common.SmsSendRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@Disabled
public class NaverSmsClientTest {

    @Autowired
    private SmsClient smsClient;

    @Value("${naver-cloud-sms.to}")
    private String to;

    @Test
    void 실제_네이버_sms_전송_테스트() {

        SmsSendRequest request = SmsSendRequest.of(
                to, "통합 테스트 메시지입니다."
        );


        StopWatch stopWatch = new StopWatch();
        stopWatch.start("send_sms");
        for(int i = 0;i<1;i++){

            long startTime = System.currentTimeMillis();
            smsClient.send(request);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("문자 전송 완료 - 수신 번호 확인");
            System.out.println(i+"번 전송 소요 시간: " + duration + "ms");
            try {
                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 인터럽트 처리
                throw new RuntimeException("Thread was interrupted", e);
            }
        }

        stopWatch.stop();

        System.out.println("문자 전송 완료 - 수신 번호 확인");
        System.out.println("전체 소요 시간(ms): " + stopWatch.getTotalTimeMillis());
    }
}
