package ru.practicum.request.model;

import ru.practicum.event.model.Event;
import ru.practicum.request.dto.RequestLogDto;
import ru.practicum.request.dto.RequestUpdateLogDto;
import ru.practicum.user.model.User;

import java.util.List;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class RequestMapper {
    public static Request toRequest(User user, Event event) {
        return new Request(
                null,
                LocalDateTime.now(),
                event,
                user,
                RequestStatus.PENDING);
    }

    public static RequestLogDto toRequestLogDto(Request request) {
        return new RequestLogDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public static List<RequestLogDto> toListRequestLogDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestLogDto)
                .collect(Collectors.toList());
    }

    public static RequestUpdateLogDto toRequestUpdateLogDto(List<Request> confirmedRequests, List<Request> rejectedRequests) {
        return new RequestUpdateLogDto(
                toListRequestLogDto(confirmedRequests),
                toListRequestLogDto(rejectedRequests));
    }
}