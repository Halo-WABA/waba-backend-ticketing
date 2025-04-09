package com.festimap.tiketing.global.error.exception;

import com.festimap.tiketing.global.error.ErrorCode;

public class AuthException extends BaseException {
    public AuthException(ErrorCode e) {
        super(e);
    }
}
