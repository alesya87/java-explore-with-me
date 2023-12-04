package ru.practicum.comment.model;

import ru.practicum.comment.dto.CommentAddDto;
import ru.practicum.comment.dto.CommentLogDto;
import ru.practicum.comment.dto.CommentShortLogDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentLogDto toCommentLogDto(Comment comment) {
        return new CommentLogDto(
                comment.getId(),
                comment.getText(),
                EventMapper.toEventShortDto(comment.getEvent()),
                UserMapper.toUserShortDto(comment.getAuthor()),
                comment.getCreatedOn(),
                comment.getPublishedOn(),
                comment.getUpdatedOn(),
                comment.getStatus());
    }

    public static Comment toComment(CommentAddDto commentAddDto, Event event, User author) {
        return new Comment(
                null,
                commentAddDto.getText(),
                event,
                author,
                LocalDateTime.now(),
                null,
                null,
                CommentStatus.PENDING
        );
    }

    public static CommentShortLogDto toCommentShortLogDto(Comment comment) {
        return new CommentShortLogDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreatedOn(),
                comment.getPublishedOn(),
                comment.getUpdatedOn());
    }

    public static List<CommentLogDto> toListCommentLogDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentLogDto)
                .collect(Collectors.toList());
    }

    public static List<CommentShortLogDto> toListCommentShortLogDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentShortLogDto)
                .collect(Collectors.toList());
    }
}