package com.festimap.tiketing.domain.event;

import com.festimap.tiketing.domain.event.dto.*;
import com.festimap.tiketing.domain.ticket.Ticket;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import lombok.AccessLevel;
import lombok.Builder;
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
@Table(
        name = "event",
        indexes = {@Index(name = "idx_festival_id", columnList = "festival_id")}
)
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "festival_id", nullable = false)
    private Long festivalId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "total_tickets", nullable = false)
    private int totalTickets;

    @Column(name = "remaining_tickets", nullable = false)
    private int remainingTickets;

    @Column(name = "open_at", nullable = false)
    private LocalDateTime openAt;

    @Column(name = "is_finished", nullable = false)
    private boolean isFinished = false;

    @Column(name = "prefix", nullable = false)
    private String prefix;

    @Column(name= "is_deleted",nullable = false)
    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets = new ArrayList<>();

    @Builder
    private Event(Long festivalId, String name, int totalTickets, int remainingTickets, LocalDateTime openAt,
                  String prefix) {
        this.festivalId = festivalId;
        this.name = name;
        this.totalTickets = totalTickets;
        this.remainingTickets = remainingTickets;
        this.openAt = openAt;
        this.prefix = prefix;
    }

    public static Event from(EventCreateReqDto eventCreateReqDto) {
        return Event.builder()
                .festivalId(eventCreateReqDto.getFestivalId())
                .name(eventCreateReqDto.getName())
                .totalTickets(eventCreateReqDto.getTotalTickets())
                .remainingTickets(eventCreateReqDto.getTotalTickets())
                .openAt(eventCreateReqDto.getOpenAt())
                .prefix(eventCreateReqDto.getPrefix())
                .build();
    }

    public void decreaseRemainingTickets(int quantity) {
        if(this.remainingTickets < quantity) {
            throw new BaseException(ErrorCode.TICKET_SOLD_OUT);
        }
        this.remainingTickets -= quantity;
        if(remainingTickets == 0) {
            isFinished = true;
        }
    }
   public void isOpen(){
        if(!LocalDateTime.now().isAfter(openAt)){
            throw new BaseException(ErrorCode.TICKET_SERVER_NOT_OPEN);
        }
    }

    public void isRemainingTicketLeft() {
        if (remainingTickets <= 0) {
            throw new BaseException(ErrorCode.TICKET_SOLD_OUT);
        }
    }
    public void updateEventInfo(EventInfoUpdateDto eventInfoUpdateDto){
        if(LocalDateTime.now().isAfter(this.getOpenAt())){
            throw new BaseException(ErrorCode.EVENT_ALREADY_OPEN);
        }
        this.name = eventInfoUpdateDto.getName();
        this.totalTickets = eventInfoUpdateDto.getTotalTickets();
        this.openAt = eventInfoUpdateDto.getOpenAt();
        this.prefix = eventInfoUpdateDto.getPrefix();
        this.remainingTickets = eventInfoUpdateDto.getTotalTickets();
    }

    public void updateName(EventNameUpdateDto eventNameUpdateDto){
        this.name = eventNameUpdateDto.getName();
    }

    public void increaseTotalTickets(EventTotalTicketsUpdateDto eventTotalTicketsUpdateDto){
        this.totalTickets += eventTotalTicketsUpdateDto.getIncreaseAmount();
        this.remainingTickets += eventTotalTicketsUpdateDto.getIncreaseAmount();
    }

    public void updateIsFinished(EventIsFinishedUpdateDto eventIsFinishedUpdateDto){
        if (eventIsFinishedUpdateDto.isFinished()) {
            this.isFinished = true;
            return;
        }

        if (this.remainingTickets <= 0) {
            throw new BaseException(ErrorCode.NO_REMAINING_TICKETS);
        }

        this.isFinished = false;
    }

    public void softDelete(){
        this.isDeleted = true;
    }
}
