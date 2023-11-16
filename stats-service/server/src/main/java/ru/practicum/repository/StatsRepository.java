package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.StatsView;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.StatsView(h.app, h.uri, count(h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "and (h.uri in (?3) or (?3) is null) " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc ")
    List<StatsView> findNotUniqueViews(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.StatsView(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "and (h.uri in (?3) or (?3) is null) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc ")
    List<StatsView> findUniqueViews(LocalDateTime start, LocalDateTime end, List<String> uris);
}