package com.festimap.tiketing.domain.ticket.service;

import com.festimap.tiketing.domain.ticket.Ticket;
import com.festimap.tiketing.domain.ticket.dto.TicketResDto;
import com.festimap.tiketing.domain.ticket.exception.TicketNotFoundException;
import com.festimap.tiketing.domain.ticket.repository.TicketRepository;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketGuestService {

    private final TicketRepository ticketRepository;

    public List<TicketResDto> getGuestTickets(Long festivalId, String phoneNumber){
        List<Ticket> tickets = ticketRepository.findAllByFestivalIdAndPhoneNumber(festivalId,phoneNumber);
        return tickets.stream()
                .map(TicketResDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    //TODO : 티켓 취소 가능 날짜 관련 개선 필요
    public void deleteReservation(String reservationNumber){
        Ticket ticket = ticketRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new TicketNotFoundException(reservationNumber));
        ticket.cancelTicket();
    }
}
