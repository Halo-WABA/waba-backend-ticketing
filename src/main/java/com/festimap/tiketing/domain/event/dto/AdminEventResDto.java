package com.festimap.tiketing.domain.event.dto;

import com.festimap.tiketing.domain.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminEventResDto {
    private Long id;
    private String name;
    private int totalTickets;
    private int remainingTickets;
    private LocalDateTime openAt;
    private boolean isFinished;
    private LocalDateTime createdAt;

    @Builder
    private AdminEventResDto(Long id, String name, int totalTickets, int remainingTickets, LocalDateTime openAt,
                            boolean isFinished, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.totalTickets = totalTickets;
        this.remainingTickets = remainingTickets;
        this.openAt = openAt;
        this.isFinished = isFinished;
        this.createdAt = createdAt;
    }

    public static AdminEventResDto from(Event event) {
        return AdminEventResDto.builder()
                .id(event.getId())
                .name(event.getName())
                .totalTickets(event.getTotalTickets())
                .remainingTickets(event.getRemainingTickets())
                .openAt(event.getOpenAt())
                .isFinished(event.isFinished())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
