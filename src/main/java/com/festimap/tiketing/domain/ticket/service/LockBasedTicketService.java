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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("lockBased")
@RequiredArgsConstructor
public class LockBasedTicketService implements TicketService{

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final VerificationRepository verificationRepository;

    @Override
    @Transactional
    public void reserve(TicketRequest request) {
        validateEventOpenAt(request);
        validateVerification(request);

        Event event = loadEventWithLockOrThrow(request.getEventId());
        isExistTicketBy(request.getEventId(), request.getPhoneNumber());
        event.decreaseRemainingTickets(request.getTicketCount());
        ticketRepository.save(Ticket.of(request,event));
    }

    private void validateVerification(TicketRequest request) {
        Verification verification = verificationRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new VerificationNotFoundException(request.getPhoneNumber()));

        verification.ensureVerificationIsValid();
    }

    private void isExistTicketBy(Long eventId, String phoneNo) {
        if(ticketRepository.findByEventIdAndPhoneNumberWithEvent(eventId, phoneNo).isPresent()){
            throw new BaseException(ErrorCode.TICKET_EXIST_BY_PHONENUM);
        }
    }

    private Event loadEventWithLockOrThrow(Long eventId) {
        return eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private void validateEventOpenAt(TicketRequest request){
        Event preview = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EventNotFoundException(request.getEventId()));
        preview.isOpen();
    }
}
