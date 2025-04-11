package com.festimap.tiketing.domain.ticket.repository;

import com.festimap.tiketing.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByPhoneNo(String phoneNo);
}
