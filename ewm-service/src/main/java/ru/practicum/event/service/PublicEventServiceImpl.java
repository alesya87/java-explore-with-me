package ru.practicum.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.util.Stats;
import ru.practicum.excception.BadRequestException;
import ru.practicum.excception.EntityNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    public PublicEventServiceImpl(EventRepository eventRepository, StatsClient statsClient) {
        this.eventRepository = eventRepository;
        this.statsClient = statsClient;
    }

    @Override
    @Transactional
    public EventLogDto getByEventId(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new EntityNotFoundException("Событие с id=" + eventId + " не найдено"));
        Stats.addView(statsClient, request.getRequestURI(), request.getRemoteAddr());
        event.setViews(Stats.getViewsCount(statsClient, event));
        return EventMapper.toEventLogDto(event);
    }

    @Override
    @Transactional
    public List<EventLogDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    Boolean onlyAvailable, EventSort sort, int from, int size, HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Дата начала должна быть позже даты окончания");
        }
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }

        List<Event> events = eventRepository.getAll(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, PageRequest.of(from / size, size));
        Stats.addView(statsClient, request.getRequestURI(), request.getRemoteAddr());

        Map<Long, Long> eventsViews = Stats.getViewsCount(statsClient, events);
        Comparator<Event> comparator = (sort == EventSort.EVENT_DATE) ?
                Comparator.comparing(Event::getEventDate) :
                Comparator.comparing(event -> eventsViews.get(event.getId()));
        events = events.stream()
                .peek(event -> event.setViews(eventsViews.get(event.getId())))
                .sorted(comparator)
                .collect(Collectors.toList());

        return EventMapper.toListEventLogDto(events);
    }
}