package ru.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitAddDto;
import ru.practicum.HitLogDto;
import ru.practicum.StatsView;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.Hit;
import ru.practicum.model.HitMapper;
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
    @Transactional(readOnly = true)
    public List<StatsView> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Дата начала должна быть раньше даты окончания");
        }
        if (unique) {
            return statsRepository.findUniqueViews(start, end, uris);
        } else {
            return statsRepository.findNotUniqueViews(start, end, uris);
        }
    }
}
