package ru.practicum.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestLogDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestController {
    private RequestService requestService;

    public PrivateRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<RequestLogDto> save(@PathVariable Long userId,
                                              @RequestParam Long eventId) {
        log.info("Получен POST-запрос к эндпоинту: '/users/{userId}/requests' на добавление запроса от пользователя с id={}" +
                " на событие с id={}", userId, eventId);
        return new ResponseEntity<>(requestService.save(userId, eventId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RequestLogDto>> getAllByUserId(@PathVariable Long userId) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{userId}/requests' от пользователя с id={} на получение списка запросов",
                userId);
        return new ResponseEntity<>(requestService.getAllByUserId(userId), HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestLogDto> cancelByUser(@PathVariable Long userId,
                                                      @PathVariable Long requestId) {
        log.info("Получен PATCH-запрос к эндпоинту: '/users/{userId}/requests/{requestId}/cancel' от пользователя с id={}" +
                        "на отмену запроса с id={}", userId, requestId);
        return new ResponseEntity<>(requestService.cancelByUser(userId, requestId), HttpStatus.OK);
    }
}