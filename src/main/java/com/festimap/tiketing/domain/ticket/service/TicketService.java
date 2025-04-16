package com.festimap.tiketing.domain.ticket.service;

import com.festimap.tiketing.domain.ticket.dto.TicketRequest;

public interface TicketService {
    void reserve(TicketRequest request);
}
