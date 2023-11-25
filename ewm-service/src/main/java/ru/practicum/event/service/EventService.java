package ru.practicum.event.service;

import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.request.dto.RequestLogDto;
import ru.practicum.request.dto.RequestUpdateLogDto;
import ru.practicum.request.dto.RequestUpdateStatusDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventLogDto save(EventAddDto eventAddDto, Long userId);

    List<EventShortDto> getAllByUserId(Long userId, int from, int size);

    EventLogDto getByUserAndEventId(Long userId, Long eventId);

    EventLogDto updateByUser(Long userId, Long eventId, EventUpdateDto eventUpdateDto);

    EventLogDto updateByAdmin(Long eventId, EventUpdateDto eventUpdateDto);

    List<RequestLogDto> getEventRequests(Long userId, Long eventId);

    RequestUpdateLogDto updateRequestStatus(Long userId, Long eventId, RequestUpdateStatusDto requestUpdateStatusDto);

    List<EventLogDto> getAllByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventLogDto getByEventId(Long eventId, HttpServletRequest request);

    List<EventLogDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                             Boolean onlyAvailable, EventSort sort, int from, int size, HttpServletRequest request);
}