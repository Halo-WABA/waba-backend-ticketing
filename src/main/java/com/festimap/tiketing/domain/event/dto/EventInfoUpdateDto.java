package com.festimap.tiketing.domain.event.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventInfoUpdateDto {
    private String name;
    private int totalTickets;
    private LocalDateTime openAt;
    private String prefix;
}
