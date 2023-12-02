package ru.practicum.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentAddDto;
import ru.practicum.comment.dto.CommentLogDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.service.PrivateCommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users/{userId}")
public class PrivateCommentController {
    private final PrivateCommentService privateCommentService;

    public PrivateCommentController(PrivateCommentService privateCommentService) {
        this.privateCommentService = privateCommentService;
    }

    @PostMapping("/events/{eventId}/comments")
    public ResponseEntity<CommentLogDto> save(@Valid @RequestBody CommentAddDto commentAddDto,
                                              @PathVariable Long userId,
                                              @PathVariable Long eventId) {
        log.info("Получен POST-запрос к эндпоинту: '/users/{userId}/events/{eventId}/comments' от пользователя с id={} " +
                        " на добавление комментария к событию с id={}: {}",
                userId, eventId, commentAddDto);
        return new ResponseEntity<>(privateCommentService.save(commentAddDto, userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentLogDto> update(@Valid @RequestBody CommentUpdateDto commentUpdateDto,
                                                @PathVariable Long userId,
                                                @PathVariable Long commentId) {
        log.info("Получен PATCH-запрос к эндпоинту: '/users/{userId}/comments/{commentId}' от пользователя с id={} " +
                " на изменение комментария с id={}", userId, commentUpdateDto);
        return new ResponseEntity<>(privateCommentService.update(commentUpdateDto, userId, commentId), HttpStatus.OK);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentLogDto>> getAll(@PathVariable Long userId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{userId}/comments' от пользователя с id={} " +
                " на получение списка комментариев", userId);
        return new ResponseEntity<>(privateCommentService.getAll(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentLogDto> get(@PathVariable Long commentId,
                                             @PathVariable Long userId) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{userId}/comments/{commentId}' от пользователя с id={} " +
                " на получение комментария с id={}", userId, commentId);
        return new ResponseEntity<>(privateCommentService.get(userId, commentId), HttpStatus.OK);
    }
}