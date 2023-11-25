package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventLogDto>> getAllByAdmin(@RequestParam(required = false) List<Long> users,
                                                           @RequestParam(required = false) List<EventState> states,
                                                           @RequestParam(required = false) List<Long> categories,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту: '/admin/events' на получение списка событий с параметрами:" +
                " users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return new ResponseEntity<>(eventService.getAllByAdmin(users, states, categories, rangeStart, rangeEnd, from, size),
                        HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventLogDto> updateByAdmin(@Valid @RequestBody EventUpdateDto eventUpdateUserDto, @PathVariable Long eventId) {
        log.info("Получен PATCH-запрос к эндпоинту: '/admin/events' на обновление события с id={}: {}", eventId, eventUpdateUserDto);
        return new ResponseEntity<>(eventService.updateByAdmin(eventId, eventUpdateUserDto), HttpStatus.OK);
    }
}