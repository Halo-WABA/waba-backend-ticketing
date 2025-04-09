package com.festimap.tiketing.global.error.exception.security;

import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.AuthException;

public class CustomAuthenticationException extends AuthException {

    public CustomAuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
