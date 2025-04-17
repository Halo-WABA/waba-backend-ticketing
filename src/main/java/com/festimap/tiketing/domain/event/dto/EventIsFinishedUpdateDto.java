package com.festimap.tiketing.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class EventIsFinishedUpdateDto {

    @NotNull(message = "isFinished는 필수 값입니다.")
    private Boolean isFinished;
}
