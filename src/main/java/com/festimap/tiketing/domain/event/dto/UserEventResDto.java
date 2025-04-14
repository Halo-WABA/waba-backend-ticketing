package com.festimap.tiketing.domain.event.dto;

import com.festimap.tiketing.domain.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEventResDto {
    private String name;
    private int remainingTickets;
    private LocalDateTime openAt;
    private boolean isFinished;

    @Builder
    private UserEventResDto(String name, int remainingTickets, LocalDateTime openAt, boolean isFinished) {
        this.name = name;
        this.remainingTickets = remainingTickets;
        this.openAt = openAt;
        this.isFinished = isFinished;
    }

    public static UserEventResDto from(Event event) {
        return UserEventResDto.builder()
                .name(event.getName())
                .remainingTickets(event.getRemainingTickets())
                .openAt(event.getOpenAt())
                .isFinished(event.isFinished())
                .build();
    }
}
