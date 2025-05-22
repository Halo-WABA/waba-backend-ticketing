package com.festimap.tiketing.domain.ticket.repository;

import com.festimap.tiketing.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t JOIN FETCH t.event WHERE t.event.id = :eventId AND t.phoneNumber = :phoneNumber")
    Optional<Ticket> findByEventIdAndPhoneNumberWithEvent(@Param("eventId") Long eventId,
                                                          @Param("phoneNumber") String phoneNumber);

    @Query("SELECT t FROM Ticket t JOIN t.event e WHERE e.festivalId = :festivalId and t.phoneNumber = :phoneNumber ")
    List<Ticket> findAllByFestivalIdAndPhoneNumber(@Param("festivalId") Long festivalId,
                                                   @Param("phoneNumber") String phoneNumber);

    @Query("SELECT t FROM Ticket t WHERE t.reservationNumber = :reservationNumber ")
    Optional<Ticket> findByReservationNumber(@Param("reservationNumber") String reservationNumber);
}
