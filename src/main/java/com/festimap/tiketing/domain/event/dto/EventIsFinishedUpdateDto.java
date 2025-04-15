package com.festimap.tiketing.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventIsFinishedUpdateDto {
    private boolean isFinished;
}
