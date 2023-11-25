package ru.practicum.request.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.excception.ConflictException;
import ru.practicum.excception.EntityNotFoundException;
import ru.practicum.request.dto.RequestLogDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository,
                              EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public RequestLogDto save(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id=" + eventId + " не найдено"));
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }
        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!Objects.equals(event.getParticipantLimit(),0L) && Objects.equals(event.getParticipantLimit(), requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED))) {
            throw new ConflictException("У события достигнут лимит запросов на участие");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя добавить запрос на участие в неопубликованном событии");
        }
        Request request = RequestMapper.toRequest(user, event);
        if (!event.getRequestModeration() || Objects.equals(event.getParticipantLimit(),0L)) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.flush();
        }
        return RequestMapper.toRequestLogDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestLogDto> getAllByUserId(Long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return RequestMapper.toListRequestLogDto(requests);
    }

    @Override
    @Transactional
    public RequestLogDto cancelByUser(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Запрос с id=" + requestId + " не найден" +
                        " для пользователя с id=" + userId + " не найден"));
        if (request.getStatus() == RequestStatus.CONFIRMED) {
            Event event = eventRepository.findById(request.getEvent().getId()).get();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.flush();
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestLogDto(requestRepository.save(request));
    }
}