package ru.practicum.model;

import ru.practicum.StatsLogDto;

import java.util.List;
import java.util.stream.Collectors;

public class StatsMapper {
    public static StatsLogDto toStatsLogDto(Stats stats) {
        return new StatsLogDto(stats.getApp(), stats.getUri(), stats.getHits());
    }

    public static List<StatsLogDto> toStatsLogListDto(List<Stats> statsList) {
        return statsList.stream()
                .map(StatsMapper::toStatsLogDto)
                .collect(Collectors.toList());
    }
}
