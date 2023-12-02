package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentLogDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminCommentService {
    void delete(Long commentId);

    CommentLogDto update(Long commentId, CommentUpdateDto commentUpdateDto);

    List<CommentLogDto> getAll(List<Long> users, String text, List<CommentStatus> statuses, List<Long> events,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);
}
