package com.festimap.tiketing.global.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class ReservationNumGenerator {

    public static String generate(String eventPrefix, int remainingTicket) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
        String formatted = String.format("%04d", remainingTicket);
        return eventPrefix + date + random + formatted;
    }
}
