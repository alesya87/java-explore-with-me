package ru.practicum.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.location.model.Location;
import ru.practicum.event.location.model.LocationMapper;
import ru.practicum.event.location.repository.LocationRepository;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.util.Stats;
import ru.practicum.excception.ConflictException;
import ru.practicum.excception.EntityNotFoundException;
import ru.practicum.request.dto.RequestLogDto;
import ru.practicum.request.dto.RequestUpdateLogDto;
import ru.practicum.request.dto.RequestUpdateStatusDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PrivateServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;

    public PrivateServiceImpl(EventRepository eventRepository, LocationRepository locationRepository,
                              UserRepository userRepository, CategoryRepository categoryRepository,
                              RequestRepository requestRepository, StatsClient statsClient) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.statsClient = statsClient;
    }

    @Override
    @Transactional
    public EventLogDto save(EventAddDto eventAddDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));
        Category category = categoryRepository.findById(eventAddDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundException("Категория с id=" + eventAddDto.getCategory() + " не найдена"));
        Location location = locationRepository.save(LocationMapper.toLocation(eventAddDto.getLocation()));
        Event event = EventMapper.toEvent(eventAddDto, category, user, location);
        return EventMapper.toEventLogDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllByUserId(Long userId, int from, int size) {
        List<Event> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size));
        events.forEach(event -> event.setViews(Stats.getViewsCount(statsClient, event)));
        return EventMapper.toListEventShortDto(events);
    }

    @Override
    @Transactional(readOnly = true)
    public EventLogDto getByUserAndEventId(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id=" + eventId + " для пользователя с id=" + userId + " не найдено"));
        event.setViews(Stats.getViewsCount(statsClient, event));
        return EventMapper.toEventLogDto(event);
    }

    @Override
    @Transactional
    public EventLogDto updateByUser(Long userId, Long eventId, EventUpdateDto eventUpdateDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id=" + eventId + " не найдено"));
        Category category = eventUpdateDto.getCategory() != null ?
                categoryRepository.findById(eventUpdateDto.getCategory())
                        .orElseThrow(() -> new EntityNotFoundException("Категория с id=" +
                                eventUpdateDto.getCategory() + " не найдена")) : null;
        Location location = eventUpdateDto.getLocation() != null ?
                locationRepository.save(LocationMapper.toLocation(eventUpdateDto.getLocation())) : null;
        if (EventState.PUBLISHED == event.getState()) {
            throw new ConflictException("Редактировать можно только отмененные и ожидающие модерации события");
        }
        if (eventUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }
        if (eventUpdateDto.getCategory() != null) {
            event.setCategory(category);
        }
        if (eventUpdateDto.getDescription() != null) {
            event.setDescription(eventUpdateDto.getDescription());
        }
        if (eventUpdateDto.getEventDate() != null) {
            event.setEventDate(eventUpdateDto.getEventDate());
        }
        if (eventUpdateDto.getLocation() != null) {
            event.setLocation(location);
        }
        if (eventUpdateDto.getPaid() != null) {
            event.setPaid(eventUpdateDto.getPaid());
        }
        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }
        if (eventUpdateDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateDto.getRequestModeration());
        }
        if (eventUpdateDto.getTitle() != null) {
            event.setTitle(eventUpdateDto.getTitle());
        }
        if (eventUpdateDto.getStateAction() == StateAction.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }
        if (eventUpdateDto.getStateAction() == StateAction.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        }
        event.setViews(Stats.getViewsCount(statsClient, event));
        return EventMapper.toEventLogDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public List<RequestLogDto> getEventRequests(Long userId, Long eventId) {
        eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id=" + eventId + " для пользователя с id=" + userId + " не найдено"));
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return RequestMapper.toListRequestLogDto(requests);
    }

    @Override
    @Transactional
    public RequestUpdateLogDto updateRequestStatus(Long userId, Long eventId, RequestUpdateStatusDto requestUpdateStatusDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id=" + eventId + " для пользователя с id=" + userId + " не найдено"));
        List<Request> requests = requestRepository.findByIdInAndEventId(requestUpdateStatusDto.getRequestIds(), eventId);
        RequestStatus status = requestUpdateStatusDto.getStatus();
        long actualLimit = event.getParticipantLimit();
        long actualConfirmRequests = event.getConfirmedRequests();
        long limitToConfirm = actualLimit == 0 ? requests.size() : actualLimit - event.getConfirmedRequests();

        if (status == RequestStatus.CONFIRMED && actualLimit != 0 && limitToConfirm <= 0) {
            throw new ConflictException("Уже достигнут лимит по заявкам на данное событие");
        }

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        if (status == RequestStatus.CONFIRMED) {
            confirmedRequests = requests.stream()
                    .limit(limitToConfirm)
                    .peek(req -> {
                        if (req.getStatus() != RequestStatus.PENDING) {
                            throw new ConflictException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
                        }
                        req.setStatus(RequestStatus.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    })
                    .collect(Collectors.toList());
        } else if (status == RequestStatus.REJECTED) {
            rejectedRequests = requests.stream()
                    .peek(req -> {
                        if (req.getStatus() != RequestStatus.PENDING) {
                            throw new ConflictException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
                        }
                        req.setStatus(RequestStatus.REJECTED);
                    })
                    .collect(Collectors.toList());
        } else {
            throw new ConflictException("Можно только подтвердить или отменить заявки");
        }
        if (actualConfirmRequests != event.getConfirmedRequests()) {
            eventRepository.flush();
        }
        return RequestMapper.toRequestUpdateLogDto(requestRepository.saveAll(confirmedRequests),
                requestRepository.saveAll(rejectedRequests));
    }
}