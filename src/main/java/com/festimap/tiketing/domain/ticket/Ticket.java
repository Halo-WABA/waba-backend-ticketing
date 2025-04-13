package com.festimap.tiketing.domain.ticket;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.global.util.ReservationNumGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDateTime issuedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder
    private Ticket(int count, String phoneNo) {
        this.count = count;
        this.reservationNumber = ReservationNumGenerator.generate();
        this.phoneNumber = phoneNo;
    }

    public static Ticket from(TicketRequest request){
        return Ticket.builder()
                .count(request.getTicketCount())
                .phoneNo(request.getPhoneNo())
                .build();
    }
}
