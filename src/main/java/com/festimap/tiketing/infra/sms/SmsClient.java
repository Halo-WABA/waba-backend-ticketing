package com.festimap.tiketing.infra.sms;

import com.festimap.tiketing.infra.sms.common.SmsSendRequest;

public interface SmsClient {
    void send(SmsSendRequest smsSendRequest);
}
