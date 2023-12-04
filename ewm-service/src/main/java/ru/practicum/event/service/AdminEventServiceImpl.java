package ru.practicum.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.location.model.Location;
import ru.practicum.event.location.model.LocationMapper;
import ru.practicum.event.location.repository.LocationRepository;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.util.Stats;
import ru.practicum.excception.ConflictException;
import ru.practicum.excception.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;

    public AdminEventServiceImpl(EventRepository eventRepository, LocationRepository locationRepository,
                                 CategoryRepository categoryRepository, StatsClient statsClient) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
        this.statsClient = statsClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventLogDto> getAllByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<Event> events = eventRepository.getAllByAdmin(users, states, categories,
                rangeStart, rangeEnd, PageRequest.of(from / size, size));
        Map<Long, Long> eventsViews = Stats.getViewsCount(statsClient, events);
        events.forEach(event -> event.setViews(eventsViews.get(event.getId())));
        return EventMapper.toListEventLogDto(events);
    }

    @Override
    @Transactional
    public EventLogDto updateByAdmin(Long eventId, EventUpdateDto eventUpdateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id=" + eventId + " не найдено"));

        if (EventState.PENDING != event.getState()) {
            throw new ConflictException("Редактировать можно только ожидающие модерации события");
        }

        Category category = eventUpdateDto.getCategory() != null ?
                categoryRepository.findById(eventUpdateDto.getCategory())
                        .orElseThrow(() -> new EntityNotFoundException("Категория с id=" +
                                eventUpdateDto.getCategory() + " не найдена")) : null;
        Location location = eventUpdateDto.getLocation() != null ?
                locationRepository.save(LocationMapper.toLocation(eventUpdateDto.getLocation())) : null;
        if (eventUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (eventUpdateDto.getDescription() != null) {
            event.setDescription(eventUpdateDto.getDescription());
        }
        if (eventUpdateDto.getEventDate() != null) {
            event.setEventDate(eventUpdateDto.getEventDate());
        }
        if (location != null) {
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
        if (eventUpdateDto.getStateAction() == StateAction.PUBLISH_EVENT) {
            event.setState(EventState.PUBLISHED);
        }
        if (eventUpdateDto.getStateAction() == StateAction.REJECT_EVENT) {
            event.setState(EventState.CANCELED);
        }
        event.setPublishedOn(LocalDateTime.now());
        event.setViews(Stats.getViewsCount(statsClient, event));
        return EventMapper.toEventLogDto(eventRepository.save(event));
    }
}