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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final BlockingQueue<TicketRequest> ticketQueue;

    @Transactional
    public void offerQueue(TicketRequest request){
        Event event = loadEventOrThrow(request.getEventId());
        event.isOpen();
        event.isRemainingTicketLeft();
        canOfferQueue(request);
    }

    @Transactional
    public void reserve(TicketRequest request) {
        isExistTicketBy(request.getEventId(), request.getPhoneNumber());
        Event event = loadEventOrThrow(request.getEventId());
        event.decreaseRemainingTickets(request.getTicketCount());
        Ticket ticket = Ticket.of(request, event);
        ticketRepository.save(ticket);
    }

    @Transactional
    public void reserveWithLock(TicketRequest request) {
        isExistTicketBy(request.getEventId(), request.getPhoneNumber());
        Event event = loadEventWithLockOrThrow(request.getEventId());
        event.decreaseRemainingTickets(request.getTicketCount());
        ticketRepository.save(Ticket.of(request,event));
    }

    private Event loadEventWithLockOrThrow(Long eventId) {
        return eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private Event loadEventOrThrow(Long eventId){
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private void isExistTicketBy(Long eventId, String phoneNo) {
        if(ticketRepository.findByEventIdAndPhoneNumberWithEvent(eventId, phoneNo).isPresent()){
            throw new BaseException(ErrorCode.TICKET_EXIST_BY_PHONENUM);
        }
    }

    private void canOfferQueue(TicketRequest request) {
        if(!ticketQueue.offer(request)){
            throw new BaseException(ErrorCode.TICKET_RESERVATION_CLOSED);
        }
    }
}
