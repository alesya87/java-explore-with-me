package ru.practicum.comment.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentAddDto;
import ru.practicum.comment.dto.CommentLogDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.comment.model.CommentStateAction;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.excception.ConflictException;
import ru.practicum.excception.EntityNotFoundException;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public PrivateCommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, EventRepository eventRepository, RequestRepository requestRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    @Transactional
    public CommentLogDto save(CommentAddDto commentAddDto, Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id=" + eventId + " не найдено"));
        if (!requestRepository.existsByRequesterIdAndEventIdAndStatus(userId, eventId, RequestStatus.CONFIRMED)) {
            throw new ConflictException("Чтобы оставить комментарий, нужно быть участником события");
        }
        Comment comment = CommentMapper.toComment(commentAddDto, event, user);
        return CommentMapper.toCommentLogDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentLogDto update(CommentUpdateDto commentUpdateDto, Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий с id=" + commentId +
                        " и автором с id=" + userId + " не найден"));
        if (comment.getStatus() == CommentStatus.PUBLISHED) {
            throw new ConflictException("Нельзя обновить подтвержденный комментарий");
        }
        if (commentUpdateDto.getCommentStateAction() == CommentStateAction.SEND_TO_REVIEW) {
            comment.setStatus(CommentStatus.PENDING);
        }
        if (commentUpdateDto.getCommentStateAction() == CommentStateAction.CANCEL_REVIEW) {
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
    public List<CommentLogDto> getAll(Long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));
        List<Comment> comments = commentRepository.findByAuthorIdOrderByCreatedOnDesc(userId, PageRequest.of(from / size, size));
        return CommentMapper.toListCommentLogDto(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentLogDto get(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий с id=" + commentId +
                        " и автором с id=" + userId + " не найден"));
        return CommentMapper.toCommentLogDto(comment);
    }
}