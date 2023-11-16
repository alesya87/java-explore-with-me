package ru.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitAddDto;
import ru.practicum.HitLogDto;
import ru.practicum.StatsLogDto;
import ru.practicum.model.Hit;
import ru.practicum.model.HitMapper;
import ru.practicum.model.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {
    private StatsRepository statsRepository;

    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    @Transactional
    public HitLogDto save(HitAddDto hitAddDto) {
        Hit hit =  statsRepository.save(HitMapper.toHit(hitAddDto));
        return HitMapper.toHitLogDto(hit);
    }

    @Override
    public List<StatsLogDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            return StatsMapper.toStatsLogListDto(statsRepository.findUniqueViews(start, end, uris));
        } else {
            return StatsMapper.toStatsLogListDto(statsRepository.findNotUniqueViews(start, end, uris));
        }
    }
}
