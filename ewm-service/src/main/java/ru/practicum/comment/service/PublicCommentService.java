package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentShortLogDto;

import java.util.List;

public interface PublicCommentService {
    List<CommentShortLogDto> getAllPublishedByEvent(Long eventId, int from, int size);
}
