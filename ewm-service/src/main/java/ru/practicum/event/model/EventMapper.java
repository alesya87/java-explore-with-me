package ru.practicum.event.model;

import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventLogDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.location.model.Location;
import ru.practicum.event.location.model.LocationMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

    public static Event toEvent(EventAddDto eventAddDto, Category category, User user, Location location) {
        return new Event(
                null,
                eventAddDto.getAnnotation(),
                category,
                0L,
                LocalDateTime.now(),
                eventAddDto.getDescription(),
                eventAddDto.getEventDate(),
                user,
                location,
                eventAddDto.getPaid() != null ? eventAddDto.getPaid() : false,
                eventAddDto.getParticipantLimit() != null ? eventAddDto.getParticipantLimit() : 0,
                null,
                eventAddDto.getRequestModeration() != null ? eventAddDto.getRequestModeration() : true,
                EventState.PENDING, eventAddDto.getTitle(),
                0L);
    }

    public static EventLogDto toEventLogDto(Event event) {
        return new EventLogDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryLogDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews());
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryLogDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }

    public static List<EventShortDto> toListEventShortDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public static List<EventLogDto> toListEventLogDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventLogDto)
                .collect(Collectors.toList());
    }
}