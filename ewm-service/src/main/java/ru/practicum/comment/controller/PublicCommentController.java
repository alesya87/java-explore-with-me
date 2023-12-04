package ru.practicum.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentShortLogDto;
import ru.practicum.comment.service.PublicCommentService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/events/{eventId}/comments")
public class PublicCommentController {
    private final PublicCommentService publicCommentService;

    public PublicCommentController(PublicCommentService publicCommentService) {
        this.publicCommentService = publicCommentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentShortLogDto>> getAllPublishedByEvent(@PathVariable Long eventId,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту: '/events/{eventId}/comments' на получение списка комментариев для:" +
                " события с id={}", eventId);
        return new ResponseEntity<>(publicCommentService.getAllPublishedByEvent(eventId, from, size),
                HttpStatus.OK);
    }
}