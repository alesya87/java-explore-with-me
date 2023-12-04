package ru.practicum.comment.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentLogDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.comment.model.CommentStateAction;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.excception.ConflictException;
import ru.practicum.excception.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminCommentServiceImpl implements AdminCommentService {
    private final CommentRepository commentRepository;

    public AdminCommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий с id=" + commentId + " не найден"));
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentLogDto update(Long commentId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий с id=" + commentId + " не найден"));
        if (comment.getStatus() == CommentStatus.PUBLISHED) {
            throw new ConflictException("Нельзя редактировать опубликованные комментарии");
        }
        if (commentUpdateDto.getCommentStateAction() == CommentStateAction.PUBLISH_COMMENT) {
            comment.setStatus(CommentStatus.PUBLISHED);
            comment.setPublishedOn(LocalDateTime.now());
        }
        if (commentUpdateDto.getCommentStateAction() == CommentStateAction.REJECT_COMMENT) {
            comment.setStatus(CommentStatus.CANCELED);
        }
        if (commentUpdateDto.getText() != null) {
            comment.setText(commentUpdateDto.getText());
        }
        comment.setUpdatedOn(LocalDateTime.now());
        return CommentMapper.toCommentLogDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentLogDto> getAll(List<Long> users, String text, List<CommentStatus> statuses, List<Long> events,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<Comment> comments = commentRepository.getAllByAdmin(users, text, statuses, events,
                rangeStart, rangeEnd, PageRequest.of(from / size, size));
        return CommentMapper.toListCommentLogDto(comments);
    }
}