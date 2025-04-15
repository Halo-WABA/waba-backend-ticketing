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
    private String prefix;

    public EventCreateReqDto(Long festivalId, String name, LocalDateTime openAt, int totalTickets, String prefix) {
        this.festivalId = festivalId;
        this.name = name;
        this.openAt = openAt;
        this.totalTickets = totalTickets;
        this.prefix = prefix;
    }
}
