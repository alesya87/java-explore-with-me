package ru.practicum.user.model;

import ru.practicum.user.dto.UserAddDto;
import ru.practicum.user.dto.UserLogDto;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toUser(UserAddDto userAddDto) {
        return new User(null, userAddDto.getName(), userAddDto.getEmail());
    }

    public static UserLogDto toUserLogDto(User user) {
        return new UserLogDto(user.getId(), user.getName(), user.getEmail());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public static List<UserLogDto> toListUserLogDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserLogDto)
                .collect(Collectors.toList());
    }
}