package com.festimap.tiketing.domain.event.exception;

import com.festimap.tiketing.global.error.exception.EntityNotFoundException;

public class EventNotFoundException extends EntityNotFoundException {
    public EventNotFoundException(Long eventId) {
        super(String.format("Event with %d is not found", eventId));
    }
}
