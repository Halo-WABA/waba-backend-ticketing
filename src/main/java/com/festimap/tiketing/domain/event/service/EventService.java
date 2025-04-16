package com.festimap.tiketing.domain.event.service;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.event.dto.*;
import com.festimap.tiketing.domain.event.exception.EventNotFoundException;
import com.festimap.tiketing.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public void create(EventCreateReqDto eventCreateReqDto){
        eventRepository.save(Event.from(eventCreateReqDto));
    }

    // TODO: 페이징 적용
    @Transactional(readOnly = true)
    public List<AdminEventItemDto> getEventsForAdmin(Long festivalId){
        return eventRepository.findAllByFestivalId(festivalId).stream()
                .map(AdminEventItemDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminEventResDto getEventForAdmin(Long id){
        Event event = loadEventOrThrow(id);
        return AdminEventResDto.from(event);
    }

    @Transactional(readOnly = true)
    public UserEventResDto getEventForUser(Long id){
        Event event = loadEventOrThrow(id);
        return UserEventResDto.from(event);
    }

    @Transactional(readOnly = true)
    public void isOpen(Long id){
        Event event = loadEventOrThrow(id);
        event.validateIsOpenAt();
    }

    @Transactional
    public AdminEventResDto updateEventInfoBeforeOpenAt(Long id, EventInfoUpdateDto eventInfoUpdateDto){
        Event event = loadEventOrThrow(id);
        event.updateEventInfo(eventInfoUpdateDto);
        return AdminEventResDto.from(event);
    }

    @Transactional
    public AdminEventResDto updateEventName(Long id, EventNameUpdateDto eventNameUpdateDto){
        Event event = loadEventOrThrow(id);
        event.updateName(eventNameUpdateDto);
        return AdminEventResDto.from(event);
    }

    @Transactional
    public AdminEventResDto increaseEventTotalTickets(Long id, EventTotalTicketsUpdateDto eventTotalTicketsUpdateDto){
        Event event = loadEventOrThrow(id);
        event.increaseTotalTickets(eventTotalTicketsUpdateDto);
        return AdminEventResDto.from(event);
    }

    @Transactional
    public AdminEventResDto updateEventIsFinished(Long id, EventIsFinishedUpdateDto eventIsFinishedUpdateDto){
        Event event = loadEventOrThrow(id);
        event.updateIsFinished(eventIsFinishedUpdateDto);
        return AdminEventResDto.from(event);
    }

    @Transactional
    public void softDeletedEvent(Long id){
        Event event = loadEventOrThrow(id);
        event.softDelete();
    }

    private Event loadEventOrThrow(Long id){
        return eventRepository.findById(id).orElseThrow(()->new EventNotFoundException(id));
    }
}
