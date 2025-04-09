package com.festimap.tiketing.global.error.exception.security;

import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.AuthException;

public class CustomAuthorizationException extends AuthException {

    public CustomAuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
