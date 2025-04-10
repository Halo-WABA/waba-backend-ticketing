package com.festimap.tiketing.infra.sms.naver.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString(of = {"requestId", "statusCode", "statusName"})
@AllArgsConstructor
@NoArgsConstructor
public class NaverSmsResDto {
    private String requestId;
    private LocalDateTime requestTime;
    private String statusCode;
    private String statusName;
}