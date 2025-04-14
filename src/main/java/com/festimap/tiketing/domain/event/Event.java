package com.festimap.tiketing.domain.event;

import com.festimap.tiketing.domain.ticket.Ticket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "event")
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "festival_id", nullable = false)
    private String festivalId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "total_tickets", nullable = false)
    private int totalTickets;

    @Column(name = "remaining_tickets", nullable = false)
    private int remainingTickets;

    @Column(name = "open_at", nullable = false)
    private LocalDateTime openAt;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets = new ArrayList<>();
}
