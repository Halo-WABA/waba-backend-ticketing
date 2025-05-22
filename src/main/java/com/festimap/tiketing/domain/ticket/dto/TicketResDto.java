package com.festimap.tiketing.domain.ticket.dto;


import com.festimap.tiketing.domain.ticket.Ticket;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TicketResDto {

    private String phoneNumber;
    private String reservationNumber;
    private String eventName;
    private int count;
    private boolean isCanceled;

    @Builder
    private TicketResDto(String phoneNumber, String reservationNumber, String eventName,
                         int count, boolean isCanceled) {
        this.phoneNumber = phoneNumber;
        this.reservationNumber = reservationNumber;
        this.eventName = eventName;
        this.count = count;
    }

    public static TicketResDto from(Ticket ticket) {
        return TicketResDto.builder()
                .phoneNumber(ticket.getPhoneNumber())
                .reservationNumber(ticket.getReservationNumber())
                .count(ticket.getCount())
                .eventName(ticket.getEvent().getName())
                .isCanceled(ticket.isCanceled())
                .build();
    }
}
