package com.festimap.tiketing.domain.ticket.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class TicketRequest {

    @NotNull(message = "이벤트Id가 누락됐습니다")
    private Long eventId;
    @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;
    @Range(min = 1, max = 2, message = "1~2장만 신청 가능합니다.")
    private int ticketCount;
}
