package com.festimap.tiketing.infra.sms.naver.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NaverMessageReqDto {
    String to;
}
