package ru.practicum.event.location.model;

import ru.practicum.event.location.dto.LocationDto;

public class LocationMapper {
    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getId(), location.getLat(), location.getLon());
    }

    public static Location toLocation(LocationDto locationDto) {
        return new Location(locationDto.getId(), locationDto.getLat(), locationDto.getLon());
    }
}
