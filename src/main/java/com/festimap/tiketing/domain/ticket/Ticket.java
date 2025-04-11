package com.festimap.tiketing.domain.ticket;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.global.util.ReservationNoGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count")
    private int count;

    @Column(name = "reservation_no")
    private String reservationNo;

    @Column(name = "phone_no")
    private String phoneNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder
    private Ticket(int count, String phoneNo) {
        this.count = count;
        this.reservationNo = ReservationNoGenerator.generate();
        this.phoneNo = phoneNo;
    }

    public static Ticket from(TicketRequest request){
        return Ticket.builder()
                .count(request.getTicketCount())
                .phoneNo(request.getPhoneNo())
                .build();
    }
}
