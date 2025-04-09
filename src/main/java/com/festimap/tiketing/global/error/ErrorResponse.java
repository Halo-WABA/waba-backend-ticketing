package com.festimap.tiketing.global.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private int status;

    private ErrorResponse(final ErrorCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.status = code.getStatus();
    }

    private ErrorResponse(final String message, final ErrorCode code) {
        this.code = code.getCode();
        this.message = message;
        this.status = code.getStatus();
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final String message, final ErrorCode code) {
        return new ErrorResponse(message, code);
    }
}
