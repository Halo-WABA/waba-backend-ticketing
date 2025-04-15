package com.festimap.tiketing.domain.ticket.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class TicketRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_입력일_경우_검증_성공() {
        TicketRequest request = new TicketRequest();
        setField(request, "eventId", 1L);
        setField(request, "phoneNumber", "01012345678");
        setField(request, "ticketCount", 2);

        Set<ConstraintViolation<TicketRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 유효하지_않은_전화번호_형식일_경우_검증_실패(){
        //given
        TicketRequest request = new TicketRequest();

        //when
        setField(request, "eventId", 1L);
        setField(request, "phoneNumber", "01199998888");
        setField(request, "ticketCount", 2);

        //then
        Set<ConstraintViolation<TicketRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("전화번호 형식이 올바르지 않습니다."));
    }

    @Test
    void 티켓수량이_범위를_벗어나면_검증_실패() {
        TicketRequest request = new TicketRequest();
        setField(request, "eventId", 1L);
        setField(request, "phoneNumber", "01012345678");
        setField(request, "ticketCount", 3);

        Set<ConstraintViolation<TicketRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("1~2장만 신청 가능합니다."));
    }

    @Test
    void 이벤트ID가_null이면_검증_실패() {
        TicketRequest request = new TicketRequest();
        setField(request, "eventId", null);
        setField(request, "phoneNumber", "01012345678");
        setField(request, "ticketCount", 1);

        Set<ConstraintViolation<TicketRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이벤트Id가 누락됐습니다"));
    }
}
