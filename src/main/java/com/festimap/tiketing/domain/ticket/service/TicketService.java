package com.festimap.tiketing.domain.ticket.service;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.event.exception.EventNotFoundException;
import com.festimap.tiketing.domain.event.repository.EventRepository;
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
    private final EventRepository eventRepository;

    @Transactional
    public void reserve(TicketRequest request) {
        isExistTicketBy(request.getEventId(), request.getPhoneNumber());
        Event event = loadEventOrThrow(request.getEventId());
        Ticket ticket = Ticket.of(request, event);
        ticketRepository.save(ticket);
    }

    @Transactional
    public void reserveWithLock(TicketRequest request) {
        isExistTicketBy(request.getEventId(), request.getPhoneNumber());
        Event event = loadEventOrThrow(request.getEventId());
        event.decreaseRemainingTickets(request.getTicketCount());
        ticketRepository.save(Ticket.of(request,event));
    }

    private Event loadEventOrThrow(Long eventId) {
        return eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private void isExistTicketBy(Long eventId, String phoneNo) {
        if(ticketRepository.existsByEventIdAndPhoneNumber(eventId, phoneNo)){
            throw new BaseException(ErrorCode.TICKET_EXIST_BY_PHONENUM);
        }
    }
}
