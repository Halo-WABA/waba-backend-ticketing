package com.festimap.tiketing.domain.ticket.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class TicketRequest {

    @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;
    private int ticketCount;
}
