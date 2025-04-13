package com.festimap.tiketing.domain.ticket.service;

import com.festimap.tiketing.domain.ticket.Ticket;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.repository.TicketRepository;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public void reserve(TicketRequest request) {
        isExistTicketBy(request.getEventId(), request.getPhoneNumber());
        Ticket ticket = Ticket.from(request);
        ticketRepository.save(ticket);
    }

    private void isExistTicketBy(Long eventId, String phoneNo) {
        if(ticketRepository.existsByEventIdAndPhoneNumber(eventId, phoneNo)){
            throw new BaseException(ErrorCode.TICKET_EXIST_BY_PHONENUM);
        }
    }
}
