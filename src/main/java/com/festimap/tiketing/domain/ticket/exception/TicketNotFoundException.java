package com.festimap.tiketing.domain.ticket.exception;

import com.festimap.tiketing.global.error.exception.EntityNotFoundException;

public class TicketNotFoundException extends EntityNotFoundException {
    public TicketNotFoundException(String reservationNumber) {
        super(String.format("Ticket with %s is not found", reservationNumber));
    }
}
