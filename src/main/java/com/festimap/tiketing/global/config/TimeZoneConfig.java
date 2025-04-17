package com.festimap.tiketing.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;

@Component
@Slf4j
public class TimeZoneConfig {

    @PostConstruct
    public void setupDefaultTimeZone() {
        ZoneId before = ZoneId.systemDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        ZoneId after = ZoneId.systemDefault();
        log.info("JVM Default TimeZone {}→{}", before, after);
    }
}
