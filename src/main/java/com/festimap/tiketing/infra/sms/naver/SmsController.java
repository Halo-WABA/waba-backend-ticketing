package com.festimap.tiketing.infra.sms.naver;

import com.festimap.tiketing.infra.sms.SmsClient;
import com.festimap.tiketing.infra.sms.common.SmsSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class SmsController {
    private final SmsClient smsClient;

    @PostMapping("/processingTime")
    public void processingTime(@RequestBody SmsSendRequest smsSendRequest) {
        smsClient.send(smsSendRequest);
    }
}
