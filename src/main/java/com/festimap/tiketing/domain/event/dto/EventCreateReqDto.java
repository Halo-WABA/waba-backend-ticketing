package com.festimap.tiketing.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventCreateReqDto {
    private Long festivalId;
    private String name;
    private LocalDateTime openAt;
    private int totalTickets;

    public EventCreateReqDto(Long festivalId, String name, LocalDateTime openAt, int totalTickets) {
        this.festivalId = festivalId;
        this.name = name;
        this.openAt = openAt;
        this.totalTickets = totalTickets;
    }
}
