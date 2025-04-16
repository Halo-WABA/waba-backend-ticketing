package com.festimap.tiketing.domain.ticket.service;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.event.exception.EventNotFoundException;
import com.festimap.tiketing.domain.event.repository.EventRepository;
import com.festimap.tiketing.domain.ticket.Ticket;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.repository.TicketRepository;
import com.festimap.tiketing.domain.verification.Verification;
import com.festimap.tiketing.domain.verification.exception.VerificationNotFoundException;
import com.festimap.tiketing.domain.verification.repository.VerificationRepository;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.BlockingQueue;


// TODO : 로직 개선 필요
@Service("queueBased")
@RequiredArgsConstructor
@Slf4j
public class QueueBasedTicketService implements TicketService{

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final VerificationRepository verificationRepository;
    private final BlockingQueue<TicketRequest> ticketQueue;

    @Override
    @Transactional
    public void reserve(TicketRequest request){
        validateEventOpenAt(request);
        validateVerification(request);
        canOfferQueue(request);
    }

    private void canOfferQueue(TicketRequest request) {
        if(!ticketQueue.offer(request)){
            throw new BaseException(ErrorCode.TICKET_SERVICE_CONGESTED);
        }
    }

    @Transactional
    public void processTicketRequest(TicketRequest request) {
        isExistTicketBy(request.getEventId(), request.getPhoneNumber());
        Event event = loadEventOrThrow(request.getEventId());
        event.decreaseRemainingTickets(request.getTicketCount());
        Ticket ticket = Ticket.of(request, event);
        ticketRepository.save(ticket);
    }

    private void validateVerification(TicketRequest request) {
        Verification verification = verificationRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new VerificationNotFoundException(request.getPhoneNumber()));

        verification.ensureVerificationIsValid();
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

    private void validateEventOpenAt(TicketRequest request){
        Event preview = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EventNotFoundException(request.getEventId()));
        preview.isOpen();
    }
}
