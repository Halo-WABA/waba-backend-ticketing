package com.festimap.tiketing.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventTotalTicketsUpdateDto {
    private int increaseAmount;
}
