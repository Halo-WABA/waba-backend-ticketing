package com.festimap.tiketing.domain.ticket.repository;

import com.festimap.tiketing.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t JOIN FETCH t.event WHERE t.event.id = :eventId AND t.phoneNumber = :phoneNumber")
    Optional<Ticket> findByEventIdAndPhoneNumberWithEvent(@Param("eventId") Long eventId, @Param("phoneNumber") String phoneNumber);
}
