package ru.practicum.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentLogDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.comment.service.AdminCommentService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {
    private final AdminCommentService adminCommentService;

    public AdminCommentController(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        log.info("Получен DELETE-запрос к эндпоинту: '/admin/comments/{commentId}' от администратора " +
                " на удаление комментария с id={}", commentId);
        adminCommentService.delete(commentId);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentLogDto> update(@PathVariable Long commentId,
                                                @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        log.info("Получен PATCH-запрос к эндпоинту: '/admin/comments/{commentId}' от администратора на обновление статуса" +
                "комментария с id={}: {}", commentId, commentUpdateDto);
        return new ResponseEntity<>(adminCommentService.update(commentId, commentUpdateDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommentLogDto>> getAll(@RequestParam(required = false) List<Long> users,
                                                      @RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<CommentStatus> statuses,
                                                      @RequestParam(required = false) List<Long> events,
                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту: '/admin/comments' на получение списка событий с параметрами:" +
                        " users={}, statuses={}, events={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, statuses, events, rangeStart, rangeEnd, from, size);
        return new ResponseEntity<>(adminCommentService.getAll(users, text, statuses, events, rangeStart, rangeEnd, from, size),
                HttpStatus.OK);
    }
}