package com.festimap.tiketing.domain.ticket.utils;


import com.festimap.tiketing.global.util.ReservationNumGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class ReservationNumGeneratorTest {

    @Test
    void 예매번호_생성() {
        int remaining = 42;
        String prefix = "MW";
        String reservationNum = ReservationNumGenerator.generate(prefix, remaining);

        System.out.println(reservationNum);
        assertEquals(17, reservationNum.length());
        assertTrue(reservationNum.matches("^MW\\d{6}[A-Z0-9]{5}\\d{4}$"));
    }
}