package com.festimap.tiketing.domain.ticket;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.global.util.ReservationNumGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ticket",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_ticket_reservation_phone",
                columnNames = {"reservation_number", "phone_number"}
        )
)
@EntityListeners(AuditingEntityListener.class)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "reservation_number", length = 10, nullable = false)
    private String reservationNumber;

    @Column(name = "phone_number", length = 11, nullable = false)
    private String phoneNumber;

    @Column(name = "issued_at", nullable = false)
    @CreatedDate
    private LocalDateTime issuedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder
    private Ticket(int count, String phoneNo, Event event) {
        this.count = count;
        this.reservationNumber = ReservationNumGenerator.generate(event.getPrefix(), event.getRemainingTickets());
        this.phoneNumber = phoneNo;
        this.event = event;
    }

    public static Ticket of(TicketRequest ticketRequest, Event event){
        return Ticket.builder()
                .count(ticketRequest.getTicketCount())
                .phoneNo(ticketRequest.getPhoneNumber())
                .event(event)
                .build();
    }
}
