package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentAddDto;
import ru.practicum.comment.dto.CommentLogDto;
import ru.practicum.comment.dto.CommentUpdateDto;

import java.util.List;

public interface PrivateCommentService {
    CommentLogDto save(CommentAddDto commentAddDto, Long userId, Long eventId);

    CommentLogDto update(CommentUpdateDto commentUpdateDto, Long userId, Long commentId);

    List<CommentLogDto> getAll(Long userId, int from, int size);

    CommentLogDto get(Long commentId, Long userId);
}
