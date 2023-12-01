package ru.practicum.event.service;

import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    EventLogDto updateByAdmin(Long eventId, EventUpdateDto eventUpdateDto);

    List<EventLogDto> getAllByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);
}