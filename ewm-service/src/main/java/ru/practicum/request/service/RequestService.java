package ru.practicum.request.service;

import ru.practicum.request.dto.RequestLogDto;

import java.util.List;

public interface RequestService {
    RequestLogDto save(Long userId, Long eventId);

    List<RequestLogDto> getAllByUserId(Long userId);

    RequestLogDto cancelByUser(Long userId, Long requestId);
}
