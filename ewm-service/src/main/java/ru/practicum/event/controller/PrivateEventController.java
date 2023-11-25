package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.RequestLogDto;
import ru.practicum.request.dto.RequestUpdateStatusDto;

import javax.validation.Valid;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;

    public PrivateEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventLogDto> save(@Valid @RequestBody EventAddDto eventAddDto, @PathVariable Long userId) {
        log.info("Получен POST-запрос к эндпоинту: '/users/{userId}/events' от пользователя с id={} на добавление события: {}",
                userId, eventAddDto);
        return new ResponseEntity<>(eventService.save(eventAddDto, userId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllByUserId(@PathVariable Long userId,
                                                              @RequestParam(defaultValue = "0") int from,
                                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{userId}/events' от пользователя с id={} на получение списка событий",
                userId);
        return new ResponseEntity<>(eventService.getAllByUserId(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventLogDto> getByUserAndEventId(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{userId}/events/{eventId}' от пользователя с id={} на получение события с id={}",
                userId, eventId);
        return new ResponseEntity<>(eventService.getByUserAndEventId(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventLogDto> updateByUser(@Valid @RequestBody EventUpdateDto eventUpdateDto,
                                                    @PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        log.info("Получен PATCH-запрос к эндпоинту: '/users/{userId}/events/{eventId}' от пользователя с id={} на обновление события с id={}: {}",
                userId, eventId, eventUpdateDto);
        return new ResponseEntity<>(eventService.updateByUser(userId, eventId, eventUpdateDto), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestLogDto>> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{userId}/events/{eventId}/requests' от пользователя с id={} на" +
                        " просмотр подтвержденных запросов для события с id={}", userId, eventId);
        return new ResponseEntity<>(eventService.getEventRequests(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestLogDto>> updateRequestStatus(@Valid @RequestBody RequestUpdateStatusDto requestUpdateStatusDto,
                                                    @PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        log.info("Получен PATCH-запрос к эндпоинту: '/users/{userId}/events/{eventId}/requests' от пользователя с id={} на обновление статуса" +
                        " запроса для события с id={}: {}", userId, eventId, requestUpdateStatusDto);
        return new ResponseEntity<>(eventService.updateRequestStatus(userId, eventId, requestUpdateStatusDto), HttpStatus.OK);
    }
}