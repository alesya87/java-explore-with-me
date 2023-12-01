package ru.practicum.model;

import ru.practicum.HitAddDto;
import ru.practicum.HitLogDto;

public class HitMapper {
    public static Hit toHit(HitAddDto hitAddDto) {
        return new Hit(
                null,
                hitAddDto.getApp(),
                hitAddDto.getUri(),
                hitAddDto.getIp(),
                hitAddDto.getTimestamp());
    }

    public static HitLogDto toHitLogDto(Hit hit) {
        return new HitLogDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp());
    }
}