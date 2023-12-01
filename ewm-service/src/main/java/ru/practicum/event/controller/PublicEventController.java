package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/events")
public class PublicEventController {
    private final PublicEventService publicEventService;

    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventLogDto> getByEventId(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Получен GET-запрос к эндпоинту: '/events/{eventId}' на получение события с id={}", eventId);
        return new ResponseEntity<>(publicEventService.getByEventId(eventId, request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventLogDto>> getAll(@RequestParam(required = false) String text,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) Boolean paid,
                                                    @RequestParam(required = false)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                    @RequestParam(required = false)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                    @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                                    @RequestParam(required = false) EventSort sort,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    HttpServletRequest request) {
        log.info("Получен GET-запрос к эндпоинту: '/events' на получение списка событий с параметрами:" +
                        " text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={} from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return new ResponseEntity<>(publicEventService.getAll(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request),
                HttpStatus.OK);
    }
}