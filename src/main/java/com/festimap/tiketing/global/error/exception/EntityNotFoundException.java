package com.festimap.tiketing.global.error.exception;

import com.festimap.tiketing.global.error.ErrorCode;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
