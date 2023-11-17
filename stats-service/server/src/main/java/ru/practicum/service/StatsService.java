package ru.practicum.service;

import ru.practicum.HitAddDto;
import ru.practicum.HitLogDto;
import ru.practicum.StatsView;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    HitLogDto save(HitAddDto hitAddDto);

    List<StatsView> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
