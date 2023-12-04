package ru.practicum.comment.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentShortLogDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.excception.EntityNotFoundException;

import java.util.List;

@Service
public class PublicCommentServiceImpl implements PublicCommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    public PublicCommentServiceImpl(CommentRepository commentRepository, EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentShortLogDto> getAllPublishedByEvent(Long eventId, int from, int size) {
        eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id=" + eventId + " не найдено"));
        List<Comment> comments = commentRepository.findByEventIdAndStatusOrderByCreatedOnDesc(eventId, CommentStatus.PUBLISHED, PageRequest.of(from / size, size));
        return CommentMapper.toListCommentShortLogDto(comments);
    }
}