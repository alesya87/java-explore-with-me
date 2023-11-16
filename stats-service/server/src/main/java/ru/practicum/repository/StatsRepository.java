package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.model.Stats(h.app, h.uri, COUNT(h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "and (h.uri in (?3) or (?3) is null) " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc ")
    List<Stats> findNotUniqueViews(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.model.Stats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "and (h.uri in (?3) or (?3) is null) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc ")
    List<Stats> findUniqueViews(LocalDateTime start, LocalDateTime end, List<String> uris);
}