package com.festimap.tiketing.infra.sms.wiremock;

import com.festimap.tiketing.infra.sms.SmsClient;
import com.festimap.tiketing.infra.sms.common.SmsSendRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WireMockController {

    private final SmsClient smsClient;

    @PostMapping("/api/sms/mockserver")
    public void mockSend(@RequestBody SmsSendRequest smsSendRequest){
        smsClient.send(smsSendRequest);
    }

    @GetMapping("/api/mock/health")
    public ResponseEntity<String> mockHealth() {
        return ResponseEntity.ok("Mock SMS Server is running");
    }
}
