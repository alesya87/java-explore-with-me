package ru.practicum.service;

import ru.practicum.HitAddDto;
import ru.practicum.HitLogDto;
import ru.practicum.StatsLogDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    HitLogDto save(HitAddDto hitAddDto);

    List<StatsLogDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
