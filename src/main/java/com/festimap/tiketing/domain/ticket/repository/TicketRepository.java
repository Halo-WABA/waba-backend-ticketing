package com.festimap.tiketing.domain.ticket.repository;

import com.festimap.tiketing.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Ticket t WHERE t.event.id = :eventId AND t.phoneNumber = :phoneNumber")
    boolean existsByEventIdAndPhoneNumber(@Param("eventId") Long eventId, @Param("phoneNumber") String phoneNumber);
}
