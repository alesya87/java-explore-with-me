package ru.practicum.event.service;

import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.model.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {

    EventLogDto getByEventId(Long eventId, HttpServletRequest request);

    List<EventLogDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                             Boolean onlyAvailable, EventSort sort, int from, int size, HttpServletRequest request);
}