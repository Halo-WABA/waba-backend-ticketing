package com.festimap.tiketing.global.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class ReservationNumGenerator {

    private static final String PREFIX = "MW";

    public static String generate() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return PREFIX + date + random;
    }
}
