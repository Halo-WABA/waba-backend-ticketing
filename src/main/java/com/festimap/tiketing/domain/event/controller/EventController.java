package com.festimap.tiketing.domain.event.controller;

import com.festimap.tiketing.domain.event.dto.*;
import com.festimap.tiketing.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    @PostMapping("/admin/event")
    public void create(@RequestBody EventCreateReqDto eventCreateReqDto) {
        eventService.create(eventCreateReqDto);
    }

    @GetMapping("/admin/events")
    public List<AdminEventItemDto> getEventsForAdmin(@RequestParam(name = "festivalId") Long festivalId){
        return eventService.getEventsForAdmin(festivalId);
    }

    @GetMapping("/admin/event")
    public AdminEventResDto getEventForAdmin(@RequestParam(name = "id") Long id){
        return eventService.getEventForAdmin(id);
    }

    @GetMapping("/event")
    public UserEventResDto getEventForUser(@RequestParam(name = "id") Long id){
        return eventService.getEventForUser(id);
    }

    @PutMapping("/admin/event")
    public AdminEventResDto updateEventInfo(@RequestParam(name = "id") Long id,
                                           @RequestBody EventInfoUpdateDto eventInfoUpdateDto){
        return eventService.updateEventInfoBeforeOpenAt(id,eventInfoUpdateDto);
    }

    @PatchMapping("/admin/event/name")
    public AdminEventResDto updateEventName(@RequestParam(name = "id") Long id,
                                            @RequestBody EventNameUpdateDto eventNameUpdateDto){
        return eventService.updateEventName(id, eventNameUpdateDto);
    }

    @PatchMapping("/admin/event/totalTickets")
    public AdminEventResDto increaseTotalTickets(@RequestParam(name = "id") Long id,
                                                 @RequestBody EventTotalTicketsUpdateDto eventTotalTicketsUpdateDto){
        return eventService.increaseEventTotalTickets(id, eventTotalTicketsUpdateDto);
    }

    @PatchMapping("/admin/event/isFinished")
    public AdminEventResDto updateEventIsFinished(@RequestParam(name = "id") Long id,
                                                  @RequestBody EventIsFinishedUpdateDto eventIsFinishedUpdateDto){
        return eventService.updateEventIsFinished(id, eventIsFinishedUpdateDto);
    }

    @DeleteMapping("/admin/event")
    public void delete(@RequestParam(name = "id") Long id){
        eventService.softDeletedEvent(id);
    }
}
