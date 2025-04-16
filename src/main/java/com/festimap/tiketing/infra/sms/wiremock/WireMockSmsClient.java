package com.festimap.tiketing.infra.sms.wiremock;

import com.festimap.tiketing.infra.sms.SmsClient;
import com.festimap.tiketing.infra.sms.common.SmsSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@Primary
public class WireMockSmsClient implements SmsClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String MOCKSERVER_URL = "http://localhost:8089/sms/send";

    @Override
    @Async("asyncExecutor")
    public void send(SmsSendRequest smsSendRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SmsSendRequest> entity = new HttpEntity<>(smsSendRequest, headers);
        try {
            long startTime = System.currentTimeMillis();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    MOCKSERVER_URL, entity, String.class);
            long endTime = System.currentTimeMillis();
            log.info("duration : {}",endTime - startTime+"ms");
            log.info("[MOCK] SMS 응답: {}", response.getBody());
        } catch (Exception e) {
            log.error("[MOCK] SMS 전송 실패", e);
        }
    }
}
