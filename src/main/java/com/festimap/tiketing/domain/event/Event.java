package com.festimap.tiketing.domain.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event")
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "festival_id")
    private String festivalId;

    @Column(name = "name")
    private String name;

    @Column(name = "total")
    private int total;

    @Column(name = "remaining")
    private int remaining;

    @Column(name = "open_at")
    private LocalDateTime openAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "event")
    private List<Event> events = new ArrayList<>();
}
