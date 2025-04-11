package com.festimap.tiketing.infra.sms.naver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festimap.tiketing.infra.sms.SmsClient;
import com.festimap.tiketing.infra.sms.common.SmsSendRequest;
import com.festimap.tiketing.infra.sms.naver.dto.NaverSmsResDto;
import com.festimap.tiketing.infra.sms.naver.util.NaverRequestBuilder;
import com.festimap.tiketing.infra.sms.naver.util.NaverUrlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class NaverSmsClient implements SmsClient {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.senderPhone}")
    private String senderPhone;

    @Value("${naver-cloud-sms.api-base-url}")
    private String apiBaseUrl;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    public NaverSmsClient(ObjectMapper objectMapper, @Qualifier("pooledRestTemplate") RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    @Async("asyncExecutor")
    public void send(SmsSendRequest smsSendRequest) {

        String endpoint = NaverUrlBuilder.buildMessageEndpoint(serviceId);
        String url = apiBaseUrl + endpoint;

        HttpEntity<String> httpEntity = NaverRequestBuilder.builder(objectMapper,accessKey,secretKey,senderPhone)
                .header(endpoint)
                .body(smsSendRequest)
                .build();
        try {
            restTemplate.postForObject(url, httpEntity, NaverSmsResDto.class);
        }
        catch(Exception e) {
            log.error("Failed to send SMS. request={}",  e.getMessage());
        }
    }
}
