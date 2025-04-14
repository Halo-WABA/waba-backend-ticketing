package com.festimap.tiketing.domain.event.dto;

import com.festimap.tiketing.domain.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminEventItemDto {
    private Long id;
    private String name;
    private boolean isFinished;
    private LocalDateTime createdAt;

    @Builder
    private AdminEventItemDto(Long id, String name, boolean isFinished, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.isFinished = isFinished;
        this.createdAt = createdAt;
    }

    public static AdminEventItemDto from(Event event) {
        return AdminEventItemDto.builder()
                .id(event.getId())
                .name(event.getName())
                .isFinished(event.isFinished())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
