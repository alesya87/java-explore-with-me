package ru.practicum.user.service;

import ru.practicum.user.dto.UserAddDto;
import ru.practicum.user.dto.UserLogDto;

import java.util.List;

public interface AdminUserService {
    UserLogDto save(UserAddDto userAddDto);

    List<UserLogDto> get(Long[] ids, int from, int size);

    void delete(Long id);
}