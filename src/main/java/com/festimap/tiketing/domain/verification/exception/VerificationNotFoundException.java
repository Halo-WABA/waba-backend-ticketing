package com.festimap.tiketing.domain.verification.exception;

import com.festimap.tiketing.global.error.exception.EntityNotFoundException;

public class VerificationNotFoundException extends EntityNotFoundException {
    public VerificationNotFoundException(String phoneNumber) {
        super(String.format("Verification with %s is not found", phoneNumber));
    }
}
