package com.festimap.tiketing.domain.ticket;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.global.util.ReservationNumGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "reservation_num", length = 10)
    private String reservationNum;

    @Column(name = "phone_num", length = 11)
    private String phoneNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder
    private Ticket(int count, String phoneNo) {
        this.count = count;
        this.reservationNum = ReservationNumGenerator.generate();
        this.phoneNum = phoneNo;
    }

    public static Ticket from(TicketRequest request){
        return Ticket.builder()
                .count(request.getTicketCount())
                .phoneNo(request.getPhoneNo())
                .build();
    }
}
