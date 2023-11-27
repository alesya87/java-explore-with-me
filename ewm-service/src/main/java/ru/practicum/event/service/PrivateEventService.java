package ru.practicum.event.service;

import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.request.dto.RequestLogDto;
import ru.practicum.request.dto.RequestUpdateLogDto;
import ru.practicum.request.dto.RequestUpdateStatusDto;

import java.util.List;

public interface PrivateEventService {
    EventLogDto save(EventAddDto eventAddDto, Long userId);

    List<EventShortDto> getAllByUserId(Long userId, int from, int size);

    EventLogDto getByUserAndEventId(Long userId, Long eventId);

    EventLogDto updateByUser(Long userId, Long eventId, EventUpdateDto eventUpdateDto);

    List<RequestLogDto> getEventRequests(Long userId, Long eventId);

    RequestUpdateLogDto updateRequestStatus(Long userId, Long eventId, RequestUpdateStatusDto requestUpdateStatusDto);
}